/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.process.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.workflow.document.repository.WfDocumentRepository
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
import co.brainz.workflow.provider.dto.ProcessListReturnDto
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
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfProcessService(
    private val wfProcessRepository: WfProcessRepository,
    private val wfProcessSimulator: WfProcessSimulator,
    private val aliceUserRepository: AliceUserRepository,
    private val wfDocumentRepository: WfDocumentRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val processMapper = Mappers.getMapper(WfProcessMapper::class.java)
    private val objMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * ???????????? ?????? ??????
     */
    fun getProcesses(processSearchCondition: ProcessSearchCondition): ProcessListReturnDto {
        val processViewDtoList = mutableListOf<RestTemplateProcessViewDto>()
        val pagingResult = wfProcessRepository.findProcessEntityList(processSearchCondition)
        val processList = pagingResult.dataList as List<WfProcessEntity>
        // ?????? / ?????? ????????? ?????? ?????? ????????? (#8969 ?????? ??????)
        for (process in processList) {
            val enabled = when (process.processStatus) {
                WfProcessConstants.Status.EDIT.code -> true
                else -> false
            }
            val processViewDto = processMapper.toProcessViewDto(process)
            processViewDto.enabled = enabled
            processViewDtoList.add(processViewDto)
        }

        return ProcessListReturnDto(
            data = processViewDtoList,
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = wfProcessRepository.count(),
                currentPageNum = processSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / processSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * ???????????? ??????
     */
    fun getProcessDetail(processId: String): RestTemplateProcessViewDto {
        val wfProcessDto = wfProcessRepository.findByProcessId(processId)?.let { processMapper.toProcessViewDto(it) }
            ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Process Entity]"
            )

        when (wfProcessDto.status) {
            WfProcessConstants.Status.EDIT.code, WfProcessConstants.Status.PUBLISH.code -> wfProcessDto.enabled = true
            WfProcessConstants.Status.USE.code -> {
                wfProcessDto.enabled = !wfProcessRepository.findProcessDocumentExist(processId)
            }
        }
        return wfProcessDto
    }

    /**
     * ???????????? ????????? ??????
     */
    fun getProcessData(processId: String): RestTemplateProcessElementDto {
        val processEntity = wfProcessRepository.findByProcessId(processId)
        val wfProcessDto = processEntity?.let { processMapper.toProcessViewDto(it) } ?: throw AliceException(
            AliceErrorConstants.ERR_00005,
            AliceErrorConstants.ERR_00005.message + "[Process Entity]"
        )
        val restTemplateElementDtoList = mutableListOf<RestTemplateElementDto>()

        wfProcessDto.createdWorkFlow = this.checkCreatedWorkFlow(processId)

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
     * ???????????? ?????? ??????
     */
    @Transactional
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
     * ???????????? 1??? ????????? ??????.
     */
    @Transactional
    fun deleteProcess(processId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val processEntity = wfProcessRepository.findByProcessId(processId)
        if (processEntity != null) {
            if (processEntity.processStatus == WfProcessConstants.Status.USE.code ||
                processEntity.processStatus == WfProcessConstants.Status.DESTROY.code
            ) {
                status = ZResponseConstants.STATUS.ERROR_EXIST
            } else {
                wfProcessRepository.deleteById(processEntity.processId)
            }
        } else {
            status = ZResponseConstants.STATUS.ERROR_NOT_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ???????????? ??????.
     */
    @Transactional
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
     * ???????????? ?????? ??????.
     */
    @Transactional
    fun updateProcessData(restTemplateProcessElementDto: RestTemplateProcessElementDto): Boolean {
        // ????????????????????? ????????? ???????????? ??????.
        val wfJsonProcessDto = restTemplateProcessElementDto.process
        val wfJsonElementsDto = restTemplateProcessElementDto.elements

        // DB??? ????????? ???????????? ????????? ???????????? ????????????????????? ????????? ????????? ????????? DB??? ????????????.
        if (wfJsonProcessDto != null) {

            // process master ????????????.
            val processEntity = wfProcessRepository.findByProcessId(wfJsonProcessDto.id) ?: throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Process Entity]"
            )

            // element data ????????????.
            processEntity.elementEntities.forEach {
                it.elementDataEntities.clear()
            }

            // element master ????????????.
            processEntity.elementEntities.clear()

            wfProcessRepository.flush()

            // process data entity ??????.
            val elementEntities = mutableListOf<WfElementEntity>()
            objMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            wfJsonElementsDto?.forEach {

                // element master entity ??????
                val elementEntity = WfElementEntity(
                    elementId = it.id,
                    processId = wfJsonProcessDto.id,
                    elementType = it.type,
                    displayInfo = objMapper.writeValueAsString(it.display),
                    elementName = it.name,
                    elementDesc = it.description,
                    notification = (it.notification == "Y")
                )

                // element data entity ??????
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
                // Script ??????
                if (it.type == WfElementConstants.ElementType.SCRIPT_TASK.value) {
                    elementEntity.elementScriptDataEntities.add(this.setScriptTaskData(it, elementEntity))
                }
                elementEntities.add(elementEntity)
            }

            // ???????????? ????????? ????????????.
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
     * ScriptTask ????????? ??????.
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
                            if (scriptDetailArray.isNotEmpty()) {
                                scriptJsonData.addProperty(
                                    WfElementConstants.AttributeId.TARGET_MAPPING_ID.value,
                                    scriptDetailArray[0]
                                )
                                if (scriptDetailArray.size > 1) {
                                    scriptJsonData.addProperty(
                                        WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value,
                                        scriptDetailArray[1]
                                    )
                                }
                            }
                        }
                        WfElementConstants.AttributeId.SCRIPT_ACTION.value -> {
                            val scriptActionArray = value.split("|")
                            val action = JsonObject()
                            if (scriptActionArray.isNotEmpty()) {
                                action.addProperty(WfElementConstants.AttributeId.CONDITION.value, scriptActionArray[0])
                                action.addProperty(WfElementConstants.AttributeId.FILE.value, scriptActionArray[1])
                                scriptActions.add(action)
                            }
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
     * [elementType] ??? ?????? [data] ??? ?????? attributeId ??? ?????? ?????? ??????.
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
     * ???????????? ?????? ?????? ??????.
     */
    @Transactional
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
     * [processId] ??? ???????????? ???????????? ?????????????????? ????????????.
     */
    fun getProcessSimulation(processId: String): SimulationReportDto {
        return wfProcessSimulator.getProcessSimulation(processId)
    }

    /**
     * ???????????? ???????????? ???????????? ?????????????????? ?????? String ?????? mutableList ??? ???????????? ??????.
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
     * [elementData] ?????? assignee type ??? ?????? assignee ?????? List ???????????? ???????????? ??????.
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
     * [elementData] ?????? target-document-list ??? List ???????????? ???????????? ??????.
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
     * ScriptTask??? ?????? ????????? ??????.
     */
    private fun getElementDataToScriptValue(element: WfElementEntity, elementData: MutableMap<String, Any>) {
        if (element.elementType == WfElementConstants.ElementType.SCRIPT_TASK.value) {
            when (elementData[WfElementConstants.AttributeId.SCRIPT_TYPE.value]) {
                WfElementConstants.ScriptType.DOCUMENT_ATTACH_FILE.value -> {
                    this.getDocumentAttachFileData(element, elementData)
                }
                WfElementConstants.ScriptType.DOCUMENT_CMDB.value -> {
                    this.getCMDBOrPluginData(element, elementData)
                }
                WfElementConstants.ScriptType.DOCUMENT_PLUGIN.value -> {
                    this.getCMDBOrPluginData(element, elementData)
                }
            }
        }
    }

    /**
     * ScriptType ??? ????????????????????? ?????? ?????? ????????? ??????.
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

    /**
     * ScriptType ??? CMDB ????????? Plugin?????? ??? ?????? ?????? ????????? ??????.
     */
    private fun getCMDBOrPluginData(element: WfElementEntity, elementData: MutableMap<String, Any>) {
        element.elementScriptDataEntities.forEach { data ->
            val scriptValue = data.scriptValue ?: ""
            if (scriptValue.isNotEmpty()) {
                val valueObject = Gson().fromJson(scriptValue, JsonObject::class.java)
                if (valueObject.get(WfElementConstants.AttributeId.TARGET_MAPPING_ID.value) != null) {
                    elementData[WfElementConstants.AttributeId.SCRIPT_DETAIL.value] = mutableListOf(
                        valueObject.get(WfElementConstants.AttributeId.TARGET_MAPPING_ID.value).asString
                    )
                }
            }
        }
    }

    fun checkCreatedWorkFlow(processId: String): Boolean {
        return wfDocumentRepository.existsByProcessId(processId)
    }
}
