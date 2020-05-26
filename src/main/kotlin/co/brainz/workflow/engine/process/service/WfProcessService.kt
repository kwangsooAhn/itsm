package co.brainz.workflow.engine.process.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.engine.process.mapper.WfProcessMapper
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.simulation.WfProcessSimulator
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.provider.dto.RestTemplateElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.util.UUID
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfProcessService(
    private val wfProcessRepository: WfProcessRepository,
    private val wfProcessSimulator: WfProcessSimulator
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val processMapper = Mappers.getMapper(WfProcessMapper::class.java)
    private val objMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 프로세스 목록 조회
     */
    fun selectProcessList(parameters: LinkedHashMap<String, Any>): MutableList<RestTemplateProcessViewDto> {
        var search = ""
        var status = listOf<String>()
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["status"] != null) status = parameters["status"].toString().split(",")
        val processViewDtoList = mutableListOf<RestTemplateProcessViewDto>()
        val processList = if (status.isEmpty()) {
            wfProcessRepository.findByProcessListOrProcessSearchList(search)
        } else {
            wfProcessRepository.findByProcessStatusInOrderByProcessName(status)
        }
        processList.forEach {
            val enabled = when (it.processStatus) {
                WfProcessConstants.Status.EDIT.code, WfProcessConstants.Status.PUBLISH.code -> true
                else -> false
            }
            val processViewDto = processMapper.toProcessViewDto(it)
            processViewDto.enabled = enabled
            processViewDtoList.add(processViewDto)
        }
        return processViewDtoList
    }

    /**
     * 프로세스 조회
     */
    fun getProcess(processId: String): RestTemplateProcessViewDto {
        val wfProcessDto = wfProcessRepository.findByProcessId(processId)?.let { processMapper.toProcessViewDto(it) }
            ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Process Entity]"
            )

        when (wfProcessDto.status) {
            WfProcessConstants.Status.EDIT.code, WfProcessConstants.Status.PUBLISH.code -> wfProcessDto.enabled = true
        }
        return wfProcessDto
    }

    /**
     * 프로세스 데이터 조회
     */
    fun getProcessData(processId: String): RestTemplateProcessElementDto {
        val processEntity = wfProcessRepository.findByProcessId(processId)
        val wfProcessDto = processEntity?.let { processMapper.toProcessViewDto(it) } ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Process Entity]"
        )
        val restTemplateElementDtoList = mutableListOf<RestTemplateElementDto>()

        for (elementEntity in processEntity.elementEntities) {
            val elDto = processMapper.toWfElementDto(elementEntity)
            elDto.display = objMapper.readValue(elementEntity.displayInfo)

            // 엘리먼트 데이터가 싱글인지 멀티값인지에 따라 String 또는 mutableList 로 저장
            val elementData = mutableMapOf<String, Any>()
            elementEntity.elementDataEntities.forEach {
                elementData[it.attributeId] = when (elementData[it.attributeId]) {
                    is String -> {
                        val data = mutableListOf(elementData[it.attributeId] as String)
                        data.add(it.attributeValue)
                        data
                    }
                    is MutableList<*> -> {
                        val data =
                            (elementData[it.attributeId] as MutableList<*>).filterIsInstance<String>().toMutableList()
                        data.add(it.attributeValue)
                        data
                    }
                    else -> {
                        it.attributeValue
                    }
                }
            }

            // assignee type 에 따라 assignee 값은 List 타입으로 리턴
            val attrIdAssigneeType = WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
            if (elementData[attrIdAssigneeType] != null) {
                val assigneeType = elementData[attrIdAssigneeType] as String
                if (assigneeType == WfTokenConstants.AssigneeType.USERS.code ||
                    assigneeType == WfTokenConstants.AssigneeType.GROUPS.code
                ) {
                    val attrIdAssignee = WfElementConstants.AttributeId.ASSIGNEE.value
                    val assignee = elementData[attrIdAssignee]
                    if (assignee is String) {
                        elementData[attrIdAssignee] = mutableListOf(assignee)
                    }
                }
            }

            // target-document-list 는 List 타입으로 리턴
            val attrIdtargetDocumentList = WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value
            if (elementData[attrIdtargetDocumentList] != null) {
                val targetDocumentList = elementData[attrIdtargetDocumentList]
                if (targetDocumentList is String) {
                    elementData[attrIdtargetDocumentList] = mutableListOf(targetDocumentList)
                }
            }

            elDto.data = elementData

            restTemplateElementDtoList.add(elDto)
        }
        return RestTemplateProcessElementDto(wfProcessDto, restTemplateElementDtoList)
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(restTemplateProcessDto: RestTemplateProcessDto): RestTemplateProcessDto {
        val wfProcessEntity: WfProcessEntity =
            wfProcessRepository.save(processMapper.toProcessEntity(restTemplateProcessDto))

        return RestTemplateProcessDto(
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
        val processEntity = wfProcessRepository.findByProcessId(processId) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Process Entity]"
        )
        if (processEntity.processStatus == WfProcessConstants.Status.PUBLISH.code ||
            processEntity.processStatus == WfProcessConstants.Status.DESTROY.code
        ) {
            return false
        } else {
            wfProcessRepository.deleteById(processEntity.processId)
        }
        return true
    }

    /**
     * 프로세스 변경.
     */
    fun updateProcess(restTemplateProcessViewDto: RestTemplateProcessViewDto): Boolean {
        val processEntity = wfProcessRepository.findByProcessId(restTemplateProcessViewDto.id) ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Process Entity]"
        )
        processEntity.processName = restTemplateProcessViewDto.name.toString()
        processEntity.processStatus = restTemplateProcessViewDto.status.toString()
        processEntity.processDesc = restTemplateProcessViewDto.description
        processEntity.updateUserKey = restTemplateProcessViewDto.updateUserKey
        processEntity.updateDt = restTemplateProcessViewDto.updateDt
        wfProcessRepository.save(processEntity)
        return true
    }

    /**
     * 프로세스 정보 변경.
     */
    fun updateProcessData(restTemplateProcessElementDto: RestTemplateProcessElementDto): Boolean {
        // 클라이언트에서 요청한 프로세스 정보.
        val wfJsonProcessDto = restTemplateProcessElementDto.process
        val wfJsonElementsDto = restTemplateProcessElementDto.elements

        // DB에 저장된 프로세스 정보를 가져와서 클라이언트에서 요청한 정보로 치환후 DB에 저장한다.
        if (wfJsonProcessDto != null) {

            // process master 조회한다.
            val processEntity = wfProcessRepository.findByProcessId(wfJsonProcessDto.id) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Process Entity]"
            )

            // element data 삭제한다.
            processEntity.elementEntities.forEach {
                it.elementDataEntities.clear()
            }

            // element master 삭제한다.
            processEntity.elementEntities.clear()

            // process data entity 생성.
            val elementEntities = mutableListOf<WfElementEntity>()
            objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            wfJsonElementsDto?.forEach {

                // element master entity 생성
                val elementEntity = WfElementEntity(
                    elementId = it.id,
                    processId = wfJsonProcessDto.id,
                    elementType = it.type,
                    displayInfo = objMapper.writeValueAsString(it.display)
                )

                // element data entity 생성
                val elementDataEntities = mutableListOf<WfElementDataEntity>()
                it.data?.entries?.forEachIndexed { idx, data ->
                    val values: MutableList<String> = objMapper.readValue(objMapper.writeValueAsString(data.value))
                    values.forEach { value ->
                        val elementDataEntity = WfElementDataEntity(
                            element = elementEntity,
                            attributeId = data.key,
                            attributeValue = value,
                            attributeOrder = idx
                        )
                        it.required?.forEach { required ->
                            if (required == data.key) {
                                elementDataEntity.attributeRequired = true
                            }
                        }
                        elementDataEntities.add(elementDataEntity)
                    }
                }
                elementEntity.elementDataEntities.addAll(elementDataEntities)
                elementEntities.add(elementEntity)
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

    /**
     * 프로세스 다음 이름 저장.
     */
    fun saveAsProcess(restTemplateProcessElementDto: RestTemplateProcessElementDto): RestTemplateProcessDto {
        val processDataDto = RestTemplateProcessDto(
            processName = restTemplateProcessElementDto.process?.name.toString(),
            processDesc = restTemplateProcessElementDto.process?.description,
            processStatus = WfProcessConstants.Status.EDIT.code,
            enabled = true,
            createDt = restTemplateProcessElementDto.process?.createDt,
            createUserKey = restTemplateProcessElementDto.process?.createUserKey,
            updateDt = restTemplateProcessElementDto.process?.updateDt,
            updateUserKey = restTemplateProcessElementDto.process?.updateUserKey
        )
        val processDto = insertProcess(processDataDto)
        val newProcess = RestTemplateProcessViewDto(
            id = processDto.processId,
            name = processDto.processName,
            createUserKey = processDto.createUserKey,
            createDt = processDto.createDt,
            status = processDto.processStatus,
            enabled = processDto.enabled,
            description = processDto.processDesc
        )
        val newElements: MutableList<RestTemplateElementDto> = mutableListOf()
        val elementKeyMap: MutableMap<String, String> = mutableMapOf()
        restTemplateProcessElementDto.elements?.forEach { element ->
            elementKeyMap[element.id] = UUID.randomUUID().toString().replace("-", "")
        }
        restTemplateProcessElementDto.elements?.forEach { element ->
            val dataMap: MutableMap<String, Any>? = element.data
            if (dataMap != null) {
                for ((key, value) in dataMap) {
                    if (elementKeyMap.containsKey(value)) {
                        dataMap[key] = elementKeyMap[value].toString()
                    }
                }
            }
            val restTemplateElementDto = RestTemplateElementDto(
                id = elementKeyMap[element.id]!!,
                data = dataMap,
                type = element.type,
                display = element.display
            )
            newElements.add(restTemplateElementDto)
        }
        val newProcessElementDto = RestTemplateProcessElementDto(
            process = newProcess,
            elements = newElements
        )
        updateProcessData(newProcessElementDto)
        return processDto
    }

    /**
     * [processId] 에 해당하는 프로세스 시뮬레이션을 실행한다.
     */
    fun getProcessSimulation(processId: String): Boolean {
        return wfProcessSimulator.getProcessSimulation(processId)
    }
}
