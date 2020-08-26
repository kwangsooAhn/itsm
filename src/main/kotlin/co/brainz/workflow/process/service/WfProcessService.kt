/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.process.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.entity.WfElementScriptDataEntity
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.dto.SimulationReportDto
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.mapper.WfProcessMapper
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.process.service.simulation.WfProcessSimulator
import co.brainz.workflow.provider.dto.RestTemplateElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessViewDto
import co.brainz.workflow.token.constants.WfTokenConstants
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.util.UUID
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfProcessService(
    private val wfProcessRepository: WfProcessRepository,
    private val wfProcessSimulator: WfProcessSimulator,
    private val aliceUserRepository: AliceUserRepository
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
        var offset = 0L
        if (parameters["search"] != null) search = parameters["search"].toString()
        if (parameters["status"] != null) status = parameters["status"].toString().split(",")
        if (parameters["offset"] != null) offset = parameters["offset"].toString().toLong()
        val processViewDtoList = mutableListOf<RestTemplateProcessViewDto>()
        val queryResult = wfProcessRepository.findProcessEntityList(search, status, offset)
        for (process in queryResult.results) {
            val enabled = when (process.processStatus) {
                WfProcessConstants.Status.EDIT.code, WfProcessConstants.Status.PUBLISH.code -> true
                else -> false
            }
            val processViewDto = processMapper.toProcessViewDto(process)
            processViewDto.enabled = enabled
            processViewDto.totalCount = queryResult.total
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
            if (elementEntity.notification) {
                elDto.notification = "Y"
            }

            val elementData = this.convertElementDataToDataType(elementEntity)
            this.convertElementDataToAssigneeType(elementData)
            this.convertElementDataToTargetDocumentList(elementData)
            this.getElementDataToScriptValue(elementEntity, elementData)

            elDto.data = elementData

            restTemplateElementDtoList.add(elDto)
        }
        return RestTemplateProcessElementDto(wfProcessDto, restTemplateElementDtoList)
    }

    /**
     * 프로세스 신규 등록
     */
    fun insertProcess(restTemplateProcessDto: RestTemplateProcessDto): RestTemplateProcessDto {
        val processEntity = processMapper.toProcessEntity(restTemplateProcessDto)
        processEntity.createUser = restTemplateProcessDto.createUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        val wfProcessEntity: WfProcessEntity = wfProcessRepository.save(processEntity)

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
    fun updateProcess(restTemplateProcessDto: RestTemplateProcessDto): Boolean {
        val processEntity =
            wfProcessRepository.findByProcessId(restTemplateProcessDto.processId) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Process Entity]"
            )
        processEntity.processName = restTemplateProcessDto.processName
        processEntity.processStatus = restTemplateProcessDto.processStatus
        processEntity.processDesc = restTemplateProcessDto.processDesc
        processEntity.updateUser = restTemplateProcessDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        processEntity.updateDt = restTemplateProcessDto.updateDt
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

            wfProcessRepository.flush()

            // process data entity 생성.
            val elementEntities = mutableListOf<WfElementEntity>()
            objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            wfJsonElementsDto?.forEach {

                // element master entity 생성
                val elementEntity = WfElementEntity(
                    elementId = it.id,
                    processId = wfJsonProcessDto.id,
                    elementType = it.type,
                    displayInfo = objMapper.writeValueAsString(it.display),
                    elementName = it.name,
                    elementDesc = it.description,
                    notification = (it.notification == "Y")
                )

                // element data entity 생성
                val elementDataEntities = mutableListOf<WfElementDataEntity>()
                it.data?.entries?.forEachIndexed { idx, data ->
                    val values: MutableList<String> = objMapper.readValue(objMapper.writeValueAsString(data.value))
                    if (!isExceptionAttributeId(it.type, data)) {
                        values.forEach { value ->
                            val elementDataEntity =
                                WfElementDataEntity(
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
                }
                elementEntity.elementDataEntities.addAll(elementDataEntities)
                // Script 저장
                if (it.type == WfElementConstants.ElementType.SCRIPT_TASK.value) {
                    elementEntity.elementScriptDataEntities.add(this.setScriptTaskData(it, elementEntity))
                }
                elementEntities.add(elementEntity)
            }

            // 프로세스 정보를 저장한다.
            processEntity.processName = wfJsonProcessDto.name.toString()
            processEntity.processStatus = wfJsonProcessDto.status.toString()
            processEntity.processDesc = wfJsonProcessDto.description
            processEntity.updateUser = wfJsonProcessDto.updateUserKey?.let {
                aliceUserRepository.findAliceUserEntityByUserKey(it)
            }
            processEntity.updateDt = wfJsonProcessDto.updateDt
            processEntity.elementEntities.addAll(elementEntities)
            wfProcessRepository.save(processEntity)
        }
        return true
    }

    /**
     * ScriptTask 데이터 저장.
     */
    private fun setScriptTaskData(
        restTemplateElementDto: RestTemplateElementDto,
        elementEntity: WfElementEntity
    ): WfElementScriptDataEntity {
        val scriptJsonData = JsonObject()
        restTemplateElementDto.data?.entries?.forEach { data ->
            val scriptActions = JsonArray()
            val values: MutableList<String> = objMapper.readValue(objMapper.writeValueAsString(data.value))
            values.forEach { value ->
                if (value.isNotEmpty()) {
                    when (data.key) {
                        WfElementConstants.AttributeId.SCRIPT_DETAIL.value -> {
                            val scriptDetailArray = value.split("|")
                            scriptJsonData.addProperty(
                                WfElementConstants.AttributeId.TARGET_MAPPING_ID.value,
                                scriptDetailArray[0]
                            )
                            scriptJsonData.addProperty(
                                WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value,
                                scriptDetailArray[1]
                            )
                        }
                        WfElementConstants.AttributeId.SCRIPT_ACTION.value -> {
                            val scriptActionArray = value.split("|")
                            val action = JsonObject()
                            action.addProperty(WfElementConstants.AttributeId.CONDITION.value, scriptActionArray[0])
                            action.addProperty(WfElementConstants.AttributeId.FILE.value, scriptActionArray[1])
                            scriptActions.add(action)
                        }
                    }
                }
            }
            scriptJsonData.add("action", scriptActions)
        }

        return WfElementScriptDataEntity(
            element = elementEntity,
            scriptId = "",
            scriptValue = Gson().toJson(scriptJsonData)
        )
    }

    /**
     * [elementType] 에 따라 [data] 의 특정 attributeId 값 존재 여부 체크.
     */
    private fun isExceptionAttributeId(elementType: String, data: MutableMap.MutableEntry<String, Any>): Boolean {
        var isExist = false
        when (elementType) {
            WfElementConstants.ElementType.SCRIPT_TASK.value -> {
                val scriptKeys = listOf(
                    WfElementConstants.AttributeId.SCRIPT_DETAIL.value,
                    WfElementConstants.AttributeId.SCRIPT_ACTION.value,
                    WfElementConstants.AttributeId.TARGET_MAPPING_ID.value,
                    WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value
                )
                if (scriptKeys.contains(data.key)) {
                    isExist = true
                }
            }
        }

        return isExist
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

            dataMap?.forEach {
                if (elementKeyMap.containsKey(it.value)) {
                    dataMap[it.key] = elementKeyMap[it.value].toString()
                }
            }

            val restTemplateElementDto = RestTemplateElementDto(
                id = elementKeyMap[element.id]!!,
                name = element.name,
                description = element.description,
                notification = element.notification,
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
    fun getProcessSimulation(processId: String): SimulationReportDto {
        return wfProcessSimulator.getProcessSimulation(processId)
    }

    /**
     * 엘리먼트 데이터가 싱글인지 멀티값인지에 따라 String 또는 mutableList 로 변환하여 리턴.
     */
    private fun convertElementDataToDataType(elementEntity: WfElementEntity): MutableMap<String, Any> {
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
        return elementData
    }

    /**
     * [elementData] 안에 assignee type 에 따라 assignee 값은 List 타입으로 변환하여 리턴.
     */
    private fun convertElementDataToAssigneeType(elementData: MutableMap<String, Any>) {
        val attrIdAssigneeType = WfElementConstants.AttributeId.ASSIGNEE_TYPE.value

        if (elementData[attrIdAssigneeType] == null) {
            return
        }

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

    /**
     * [elementData] 안에 target-document-list 는 List 타입으로 변환하여 리턴.
     */
    private fun convertElementDataToTargetDocumentList(elementData: MutableMap<String, Any>) {
        val attrIdTargetDocumentList = WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value
        if (elementData[attrIdTargetDocumentList] == null) {
            return
        }
        val targetDocumentList = elementData[attrIdTargetDocumentList]
        if (targetDocumentList is String) {
            elementData[attrIdTargetDocumentList] = mutableListOf(targetDocumentList)
        }
    }

    /**
     * ScriptTask의 상세 데이터 조회.
     */
    private fun getElementDataToScriptValue(element: WfElementEntity, elementData: MutableMap<String, Any>) {
        if (element.elementType == WfElementConstants.ElementType.SCRIPT_TASK.value) {
            when (elementData[WfElementConstants.AttributeId.SCRIPT_TYPE.value]) {
                WfElementConstants.ScriptType.DOCUMENT_ATTACH_FILE.value -> {
                    this.getDocumentAttachFileData(element, elementData)
                }
            }
        }
    }

    /**
     * ScriptType 이 문서첨부파일인 경우 상세 데이터 조회.
     */
    private fun getDocumentAttachFileData(element: WfElementEntity, elementData: MutableMap<String, Any>) {
        element.elementScriptDataEntities.forEach { data ->
            val scriptValue = data.scriptValue ?: ""
            if (scriptValue.isNotEmpty()) {
                val valueObject = Gson().fromJson(scriptValue, JsonObject::class.java)
                if (valueObject.get(WfElementConstants.AttributeId.TARGET_MAPPING_ID.value) != null &&
                    valueObject.get(WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value) != null
                ) {
                    elementData[WfElementConstants.AttributeId.SCRIPT_DETAIL.value] = mutableListOf(
                        valueObject.get(WfElementConstants.AttributeId.TARGET_MAPPING_ID.value).asString +
                                "|" +
                                valueObject.get(WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value).asString
                    )
                }
                if (valueObject.getAsJsonArray(WfElementConstants.AttributeId.ACTION.value) != null &&
                    valueObject.getAsJsonArray(WfElementConstants.AttributeId.ACTION.value).isJsonArray
                ) {
                    val actionArray =
                        valueObject.getAsJsonArray(WfElementConstants.AttributeId.ACTION.value).asJsonArray
                    val actionList: MutableList<String> = mutableListOf()
                    actionArray.forEach { action ->
                        actionList.add(
                            action.asJsonObject.get(WfElementConstants.AttributeId.CONDITION.value).asString +
                                    "|" + action.asJsonObject.get(WfElementConstants.AttributeId.FILE.value).asString
                        )
                    }
                    elementData[WfElementConstants.AttributeId.SCRIPT_ACTION.value] = actionList
                }
            }
        }
    }
}
