package co.brainz.workflow.process.service

import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.dto.ProcessDto
import co.brainz.workflow.process.dto.WfJsonElementDto
import co.brainz.workflow.process.dto.WfJsonMainDto
import co.brainz.workflow.process.dto.WfJsonProcessDto
import co.brainz.workflow.process.entity.ElementDataEntity
import co.brainz.workflow.process.entity.ElementMstEntity
import co.brainz.workflow.process.entity.ProcessMstEntity
import co.brainz.workflow.process.mapper.ProcessMstMapper
import co.brainz.workflow.process.repository.ElementMstRepository
import co.brainz.workflow.process.repository.ProcessMstRepository
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
class WFProcessService(
    private val processMstRepository: ProcessMstRepository,
    private val elementMstRepository: ElementMstRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val processMstMapper = Mappers.getMapper(ProcessMstMapper::class.java)

    /**
     * 프로세스 목록 조회
     */
    fun selectProcessList(search: String): MutableList<WfJsonProcessDto> {
        val processDtoList = mutableListOf<WfJsonProcessDto>()
        val procMstList = if (search.isEmpty()) {
            processMstRepository.findAll()
        } else {
            val word = "%$search%"
            processMstRepository.findByProcessNameLikeOrProcessDescLike(word, word)
        }

        procMstList.forEach {
            val enabled = when (it.processStatus) {
                ProcessConstants.Status.EDIT.code, ProcessConstants.Status.SIMULATION.code -> true
                else -> false
            }
            val wfProcessDto = processMstMapper.toWfJsonProcessDto(it)
            wfProcessDto.enabled = enabled
            processDtoList.add(wfProcessDto)
        }
        return processDtoList
    }

    /**
     * 프로세스 조회
     */
    fun getProcess(processId: String): WfJsonMainDto {
        val processMstEntity = processMstRepository.findProcessMstEntityByProcessId(processId)
        val wfProcessDto = processMstMapper.toWfJsonProcessDto(processMstEntity)
        val wfElementDto = mutableListOf<WfJsonElementDto>()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val convertMap = mutableMapOf<String, Any>()

        for (elementMstEntity in processMstEntity.elementMstEntity) {
            val elDto = processMstMapper.toWfJsonElementDto(elementMstEntity)
            elDto.display = elementMstEntity.displayInfo?.let { mapper.readValue(it) }
            elDto.data = elementMstEntity.elementDataEntity.associateByTo(convertMap, { it.attrId }, { it.attrValue })
            wfElementDto.add(elDto)
        }
        return WfJsonMainDto(wfProcessDto, wfElementDto)
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(processDto: ProcessDto): ProcessDto {
        processDto.processStatus = ProcessConstants.Status.EDIT.code // 등록 시 프로세스 상태
        val processMstEntity: ProcessMstEntity =
            processMstRepository.save(processMstMapper.toProcessMstEntity(processDto))

        return ProcessDto(
            processMstEntity.processId,
            processMstEntity.processName,
            processMstEntity.processDesc,
            processMstEntity.processStatus,
            "",
            "",
            processMstEntity.createDt,
            "",
            processMstEntity.updateDt,
            "",
            false
        )
    }

    /**
     * 프로세스 1건 데이터 삭제.
     */
    fun deleteProcess(processId: String): Boolean {
        val processMstEntity = processMstRepository.findProcessMstEntityByProcessId(processId)
        if (processMstEntity.processStatus.equals(ProcessConstants.Status.PUBLISH)
            || processMstEntity.processStatus.equals(ProcessConstants.Status.DESTROY)
        ) {
            return false
        } else {
            processMstRepository.deleteById(processMstEntity.processId)
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
            val processMstEntity = processMstRepository.findProcessMstEntityByProcessId(wfJsonProcessDto.id)

            // element data 삭제한다.
            processMstEntity.elementMstEntity.forEach {
                it.elementDataEntity.clear()
            }

            // element master 삭제한다.
            processMstEntity.elementMstEntity.clear()

            // process data entity 생성.
            val elementMstEntities = mutableListOf<ElementMstEntity>()
            if (wfJsonElementsDto != null) {
                val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

                wfJsonElementsDto.forEach {

                    // element master entity 생성
                    var elementMstEntity = ElementMstEntity(
                        procId = wfJsonProcessDto.id,
                        displayInfo = mapper.writeValueAsString(it.display)
                    )

                    // 시스템이 만들어내는 element id 획득을 위해 save 를 실행한다.
                    elementMstEntity = elementMstRepository.save(elementMstEntity)

                    // element data entity 생성
                    val elementDataEntities = mutableListOf<ElementDataEntity>()
                    it.data?.entries?.forEachIndexed { idx, data ->
                        elementDataEntities.add(
                            ElementDataEntity(
                                elemId = elementMstEntity.elemId,
                                attrId = data.key,
                                attrValue = data.value as String,
                                attrOrder = idx
                            )
                        )
                    }
                    elementMstEntity.elementDataEntity.addAll(elementDataEntities)
                    elementMstEntities.add(elementMstEntity)
                }
            }

            // 프로세스 정보를 저장한다.
            processMstEntity.processName = wfJsonProcessDto.name.toString()
            processMstEntity.processDesc = wfJsonProcessDto.description
            processMstEntity.elementMstEntity.addAll(elementMstEntities)
            processMstRepository.save(processMstEntity)

        }
        return true
    }
}
