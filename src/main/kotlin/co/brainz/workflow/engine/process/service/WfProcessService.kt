package co.brainz.workflow.engine.process.service

import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.dto.ProcessDto
import co.brainz.workflow.engine.process.dto.WfElementDto
import co.brainz.workflow.engine.process.dto.WfProcessDto
import co.brainz.workflow.engine.process.dto.WfProcessElementDto
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.engine.process.mapper.WfProcessMapper
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfProcessService(private val wfProcessRepository: WfProcessRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val processMapper = Mappers.getMapper(WfProcessMapper::class.java)

    /**
     * 프로세스 목록 조회
     */
    fun selectProcessList(parameters: LinkedHashMap<String, Any>): MutableList<WfProcessDto> {
        var search: String = ""
        var status: String = ""
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["status"] != null) status = parameters["status"].toString()
        val processDtoList = mutableListOf<WfProcessDto>()
        val processList = if (search.isEmpty() && status.isEmpty()) {
            wfProcessRepository.findAll()
        } else if (status.isEmpty()){
            wfProcessRepository.findByProcessNameLikeOrProcessDescLike("%$search%", "%$search%")
        } else wfProcessRepository.findByProcessStatusLike(status)

        processList.forEach {
            val enabled = when (it.processStatus) {
                WfProcessConstants.Status.EDIT.code, WfProcessConstants.Status.SIMULATION.code -> true
                else -> false
            }
            val wfProcessDto = processMapper.toWfProcessDto(it)
            wfProcessDto.enabled = enabled
            processDtoList.add(wfProcessDto)
        }
        return processDtoList
    }

    /**
     * 프로세스 조회
     */
    fun getProcess(processId: String): WfProcessElementDto {
        val processEntity = wfProcessRepository.findByProcessId(processId)
        val wfProcessDto = processMapper.toWfProcessDto(processEntity)
        val wfElementDto = mutableListOf<WfElementDto>()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        for (elementEntity in processEntity.elementEntities) {
            val elDto = processMapper.toWfElementDto(elementEntity)
            elDto.display = elementEntity.displayInfo.let { mapper.readValue(it) }
            elDto.data = elementEntity.elementDataEntities.associateByTo(
                mutableMapOf(),
                { it.attributeId },
                { it.attributeValue })
            wfElementDto.add(elDto)
        }
        return WfProcessElementDto(wfProcessDto, wfElementDto)
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(processDto: ProcessDto): ProcessDto {
        processDto.processStatus = WfProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태
        val wfProcessEntity: WfProcessEntity = wfProcessRepository.save(processMapper.toProcessEntity(processDto))

        return ProcessDto(
            processId = wfProcessEntity.processId,
            processName = wfProcessEntity.processName,
            processDesc = wfProcessEntity.processDesc,
            processStatus = wfProcessEntity.processStatus,
            createDt = wfProcessEntity.createDt,
            updateDt = wfProcessEntity.updateDt,
            enabled = false
        )
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        val processEntity = wfProcessRepository.findByProcessId(processId)
        if (processEntity.processStatus == WfProcessConstants.Status.PUBLISH.code
            || processEntity.processStatus == WfProcessConstants.Status.DESTROY.code
        ) {
            return false
        } else {
            wfProcessRepository.deleteById(processEntity.processId)
        }
        return true
    }

    /**
     * 프로세스 정보 변경.
     */
    fun updateProcess(wfProcessElementDto: WfProcessElementDto): Boolean {

        // 클라이언트에서 요청한 프로세스 정보.
        val wfJsonProcessDto = wfProcessElementDto.process
        val wfJsonElementsDto = wfProcessElementDto.elements

        // DB에 저장된 프로세스 정보를 가져와서 클라이언트에서 요청한 정보로 치환후 DB에 저장한다.
        if (wfJsonProcessDto != null) {

            // process master 조회한다.
            val processEntity = wfProcessRepository.findByProcessId(wfJsonProcessDto.id)

            // element data 삭제한다.
            processEntity.elementEntities.forEach {
                it.elementDataEntities.clear()
            }

            // element master 삭제한다.
            processEntity.elementEntities.clear()

            // process data entity 생성.
            val elementEntities = mutableListOf<WfElementEntity>()
            if (wfJsonElementsDto != null) {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

                wfJsonElementsDto.forEach {

                    // element master entity 생성
                    val elementEntity = WfElementEntity(
                        elementId = it.id,
                        processId = wfJsonProcessDto.id,
                        elementType = it.type,
                        displayInfo = mapper.writeValueAsString(it.display)
                    )

                    // element data entity 생성
                    val elementDataEntities = mutableListOf<WfElementDataEntity>()
                    it.data?.entries?.forEachIndexed { idx, data ->
                        elementDataEntities.add(
                            WfElementDataEntity(
                                element = elementEntity,
                                attributeId = data.key,
                                attributeValue = data.value as String,
                                attributeOrder = idx
                            )
                        )
                    }
                    elementEntity.elementDataEntities.addAll(elementDataEntities)
                    elementEntities.add(elementEntity)
                }
            }

            // 프로세스 정보를 저장한다.
            processEntity.processName = wfJsonProcessDto.name.toString()
            processEntity.processStatus = wfJsonProcessDto.status.toString()
            processEntity.processDesc = wfJsonProcessDto.description
            processEntity.updateUserKey = wfJsonProcessDto.updateUserKey
            processEntity.updateDt = wfJsonProcessDto.updateDt
            processEntity.elementEntities.addAll(elementEntities)
            wfProcessRepository.save(processEntity)

        }
        return true
    }
}
