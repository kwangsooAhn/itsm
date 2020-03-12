package co.brainz.workflow.process.service

import co.brainz.workflow.element.entity.ElementDataEntity
import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonElementDto
import co.brainz.workflow.process.dto.WfJsonMainDto
import co.brainz.workflow.process.dto.WfJsonProcessDto
import co.brainz.workflow.process.entity.ProcessEntity
import co.brainz.workflow.process.mapper.ProcessMapper
import co.brainz.workflow.process.repository.ProcessRepository
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
class WFProcessService(private val processRepository: ProcessRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val processMapper = Mappers.getMapper(ProcessMapper::class.java)

    /**
     * 프로세스 목록 조회
     */
    fun selectProcessList(search: String): MutableList<WfJsonProcessDto> {
        val processDtoList = mutableListOf<WfJsonProcessDto>()
        val processList = if (search.isEmpty()) {
            processRepository.findAll()
        } else {
            val word = "%$search%"
            processRepository.findByProcessNameLikeOrProcessDescLike(word, word)
        }

        processList.forEach {
            val enabled = when (it.processStatus) {
                ProcessConstants.Status.EDIT.code, ProcessConstants.Status.SIMULATION.code -> true
                else -> false
            }
            val wfProcessDto = processMapper.toWfJsonProcessDto(it)
            wfProcessDto.enabled = enabled
            processDtoList.add(wfProcessDto)
        }
        return processDtoList
    }

    /**
     * 프로세스 조회
     */
    fun getProcess(processId: String): WfJsonMainDto {
        val processEntity = processRepository.findProcessEntityByProcessId(processId)
        val wfProcessDto = processMapper.toWfJsonProcessDto(processEntity)
        val wfElementDto = mutableListOf<WfJsonElementDto>()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        for (elementEntity in processEntity.elementEntities) {
            val elDto = processMapper.toWfJsonElementDto(elementEntity)
            elDto.display = elementEntity.displayInfo.let { mapper.readValue(it) }
            elDto.data = elementEntity.elementDataEntities.associateByTo(
                mutableMapOf(),
                { it.attributeId },
                { it.attributeValue })
            wfElementDto.add(elDto)
        }
        return WfJsonMainDto(wfProcessDto, wfElementDto)
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(processDto: ProcessDto): ProcessDto {
        processDto.processStatus = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태
        val processEntity: ProcessEntity =
            processRepository.save(processMapper.toProcessEntity(processDto))

        return ProcessDto(
            processId = processEntity.processId,
            processName = processEntity.processName,
            processDesc = processEntity.processDesc,
            processStatus = processEntity.processStatus,
            createDt = processEntity.createDt,
            updateDt = processEntity.updateDt,
            enabled = false
        )
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        val processEntity = processRepository.findProcessEntityByProcessId(processId)
        if (processEntity.processStatus == ProcessConstants.Status.PUBLISH.code
            || processEntity.processStatus == ProcessConstants.Status.DESTROY.code
        ) {
            return false
        } else {
            processRepository.deleteById(processEntity.processId)
        }
        return true
    }

    /**
     * 프로세스 정보 변경.
     */
    fun updateProcess(wfJsonMainDto: WfJsonMainDto): Boolean {

        // 클라이언트에서 요청한 프로세스 정보.
        val wfJsonProcessDto = wfJsonMainDto.process
        val wfJsonElementsDto = wfJsonMainDto.elements

        // DB에 저장된 프로세스 정보를 가져와서 클라이언트에서 요청한 정보로 치환후 DB에 저장한다.
        if (wfJsonProcessDto != null) {

            // process master 조회한다.
            val processEntity = processRepository.findProcessEntityByProcessId(wfJsonProcessDto.id)

            // element data 삭제한다.
            processEntity.elementEntities.forEach {
                it.elementDataEntities.clear()
            }

            // element master 삭제한다.
            processEntity.elementEntities.clear()

            // process data entity 생성.
            val elementEntities = mutableListOf<ElementEntity>()
            if (wfJsonElementsDto != null) {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

                wfJsonElementsDto.forEach {

                    // element master entity 생성
                    val elementEntity = ElementEntity(
                        elementId = it.id,
                        processId = wfJsonProcessDto.id,
                        elementType = it.type,
                        displayInfo = mapper.writeValueAsString(it.display)
                    )

                    // element data entity 생성
                    val elementDataEntities = mutableListOf<ElementDataEntity>()
                    it.data?.entries?.forEachIndexed { idx, data ->
                        elementDataEntities.add(
                            ElementDataEntity(
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
            processRepository.save(processEntity)

        }
        return true
    }
}
