/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.impl

import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIDataDto
import co.brainz.cmdb.dto.CIDataForGroupListDto
import co.brainz.cmdb.dto.CIDto
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.framework.resourceManager.constants.ResourceConstants
import co.brainz.framework.resourceManager.entity.AliceFileLocEntity
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.itsm.cmdb.ci.constants.CIConstants
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.plugin.constants.PluginConstants
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.constants.WorkflowConstants
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.slf4j.LoggerFactory

class WfScriptTask(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = true
) : WfTokenManager(wfTokenManagerService) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

    private val listLinkedMapType: CollectionType =
        TypeFactory.defaultInstance().constructCollectionType(
            List::class.java,
            TypeFactory.defaultInstance().constructMapType(
                LinkedHashMap::class.java,
                String::class.java,
                Any::class.java
            )
        )

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        var scriptType = ""
        val element = wfTokenManagerService.getElement(createTokenDto.elementId)

        run loop@{
            element.elementDataEntities.forEach { data ->
                if (data.attributeId == WfElementConstants.AttributeId.SCRIPT_TYPE.value) {
                    scriptType = data.attributeValue
                    return@loop
                }
            }
        }

        // ????????????(??????) ????????? ????????? ???????????? ScriptTask ??????
        if (createTokenDto.documentStatus == DocumentConstants.DocumentStatus.USE.value) {
            when (scriptType) {
                WfElementConstants.ScriptType.DOCUMENT_ATTACH_FILE.value ->
                    this.setDocumentAttachFile(createTokenDto, element)
                WfElementConstants.ScriptType.DOCUMENT_CMDB.value -> {
                    this.callCmdbAction(createTokenDto, element)
                }
                WfElementConstants.ScriptType.DOCUMENT_PLUGIN.value -> {
                    this.executePlugin(createTokenDto, element)
                }
            }
        }

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(createNextTokenDto)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        return completedToken
    }

    /**
     * [createTokenDto.data] ?????? ????????? ?????? (TokenData??? ????????? CI ?????????).
     */
    private fun getCiList(createTokenDto: WfTokenDto, componentEntity: WfComponentEntity): List<Map<String, Any>> {
        val tokenData =
            createTokenDto.data?.filter { it.componentId == componentEntity.componentId }?.get(0)
        return mapper.readValue(tokenData?.value, listLinkedMapType)
    }

    /**
     * [ciAttributes] ??? CiDto ??? ???????????? ?????? List ????????? ??????.
     */
    private fun getCiDataList(ciId: String, ciComponentDataValue: Map<String, Any>): MutableList<CIDataDto> {
        val ciDataList = mutableListOf<CIDataDto>()
        val ciAttributes: List<Map<String, Any>> =
            mapper.convertValue(ciComponentDataValue["ciAttributes"], listLinkedMapType)
        ciAttributes.forEach { attribute ->
            if (attribute["id"] != null && attribute["value"] != null) {
                // Group List ????????? ??????
                val ciDataForGroupLists = mutableListOf<CIDataForGroupListDto>()
                if (attribute["type"] == RestTemplateConstants.AttributeType.GROUP_LIST.code) {
                    val childAttributes: List<Map<String, Any>> =
                        mapper.convertValue(attribute["childAttributes"], listLinkedMapType)
                    childAttributes.forEach { childAttribute ->
                        ciDataForGroupLists.add(
                            CIDataForGroupListDto(
                                ciId = ciId,
                                attributeId = attribute["id"] as String,
                                cAttributeId = childAttribute["id"] as String,
                                cAttributeSeq = childAttribute["seq"] as Int,
                                cValue = childAttribute["value"] as String
                            )
                        )
                    }
                }
                ciDataList.add(
                    CIDataDto(
                        ciId = ciId,
                        attributeId = attribute["id"] as String,
                        attributeData = attribute["value"] as String,
                        childAttributes = ciDataForGroupLists
                    )
                )
            }
        }
        return ciDataList
    }

    /**
     * [ciTags] ??? CIDto ??? ???????????? ?????? List ????????? ??????.
     */
    private fun getCiTags(ciId: String, ciComponentDataValue: Map<String, Any>): JsonArray {
        val tagDataList = JsonArray()
        val ciTags: List<Map<String, Any>> =
            mapper.convertValue(ciComponentDataValue["ciTags"], listLinkedMapType)
        ciTags.forEach { tag ->
            if (tag["id"] != null && tag["value"] != null) {
                val ciTag = JsonObject()
                ciTag.addProperty("tagId", tag["id"] as String)
                ciTag.addProperty("tagType", AliceTagConstants.TagType.CI.code)
                ciTag.addProperty("tagValue", tag["value"] as String)
                ciTag.addProperty("targetId", ciId)
                tagDataList.add(ciTag)
            }
        }
        return tagDataList
    }

    /**
     * ????????? CI ??? CIDto ??? ???????????? ?????? List ????????? ??????.
     */
    private fun getCiRelations(ciComponentDataValue: Map<String, Any>): MutableList<CIRelationDto> {
        val relationList = mutableListOf<CIRelationDto>()
        val relationData = mutableListOf<Map<String, String>>()

        relationData.addAll(mapper.convertValue(ciComponentDataValue["relatedCIData"], listLinkedMapType))
        relationData.forEach {
            relationList.add(mapper.convertValue(it, CIRelationDto::class.java))
        }
        return relationList
    }

    /**
     * CMDB CI Rest Api ????????? ????????? ????????? CI ????????? ??????.
     */
    private fun getCiDtoList(
        ciList: List<Map<String, Any>>,
        componentId: String,
        instanceId: String,
        actionType: String
    ): MutableList<CIDto> {
        val ciDtoList = mutableListOf<CIDto>()
        ciList.forEach { ci ->
            if (ci["actionType"] as String == actionType) {
                val ciId = ci["ciId"] as String
                val ciComponentData =
                    wfTokenManagerService.getComponentCIData(componentId, ciId, instanceId)
                when (actionType) {
                    CIConstants.ActionType.REGISTER.code, CIConstants.ActionType.MODIFY.code -> {
                        if (ciComponentData !== null) {
                            val ciComponentDataValue: Map<String, Any> =
                                mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})

                            // CI??? ?????? ?????? ?????? ??????
                            val ciDataList = this.getCiDataList(ciId, ciComponentDataValue)
                            val ciTags = this.getCiTags(ciId, ciComponentDataValue)
                            val ciRelations = this.getCiRelations(ciComponentDataValue)

                            // Dto ????????? List??? ??????
                            val ciDto = CIDto(
                                ciId = ciId,
                                ciNo = ci["ciNo"] as String,
                                ciName = ci["ciName"] as String,
                                ciDesc = ci["ciDesc"] as String,
                                ciIcon = ci["ciIcon"] as String,
                                classId = ci["classId"] as String,
                                typeId = ci["typeId"] as String,
                                ciStatus = ci["ciStatus"] as String,
                                instanceId = instanceId,
                                ciDataList = ciDataList,
                                ciTags = ciTags,
                                ciRelations = ciRelations,
                                createUserKey = super.assigneeId,
                                updateUserKey = super.assigneeId,
                                interlink = ci["interlink"].toString().toBoolean()
                            )
                            if (ci["mappingId"] != null) {
                                ciDto.mappingId = ci["mappingId"].toString()
                            }
                            ciDtoList.add(ciDto)
                        }
                    }
                    CIConstants.ActionType.DELETE.code -> {
                        ciDtoList.add(
                            CIDto(
                                ciId = ciId,
                                typeId = "",
                                instanceId = instanceId,
                                updateUserKey = super.assigneeId
                            )
                        )
                    }
                }
            }
        }
        return ciDtoList
    }

    /**
     * 1. ?????? Token ??? Component ?????? ??? ci ??????????????? ??????
     * 2. ????????? CI Component ????????? ??????????????? ??????????????? CI ??????
     */
    private fun callCmdbAction(createTokenDto: WfTokenDto, element: WfElementEntity) {
        val ciComponents = this.getCIComponents(createTokenDto)
        for (ciComponent in ciComponents) {
            val ciList = this.getCiList(createTokenDto, ciComponent)
            this.getCiDtoList(
                ciList, ciComponent.componentId, createTokenDto.instanceId, RestTemplateConstants.ActionType.REGISTER.code
            ).forEach { wfTokenManagerService.createCI(it) }
            this.getCiDtoList(
                ciList, ciComponent.componentId, createTokenDto.instanceId, RestTemplateConstants.ActionType.MODIFY.code
            ).forEach { wfTokenManagerService.updateCI(it) }
            this.getCiDtoList(
                ciList, ciComponent.componentId, createTokenDto.instanceId, RestTemplateConstants.ActionType.DELETE.code
            ).forEach { wfTokenManagerService.deleteCI(it) }
        }
    }

    private fun executePlugin(createTokenDto: WfTokenDto, element: WfElementEntity) {
        // scriptTask ??????????????? pluginId ??????
        var scriptValue: Map<String, String> = emptyMap()
        element.elementScriptDataEntities.forEach {
            scriptValue = mapper.readValue(it.scriptValue, object : TypeReference<Map<String, Any>>() {})
        }
        val pluginId = scriptValue[WfElementConstants.AttributeId.TARGET_MAPPING_ID.value]
        val param: LinkedHashMap<String, Any> = linkedMapOf()
        param[PluginConstants.ASYNCHRONOUS] = false
        if (pluginId != null) {
            wfTokenManagerService.executePlugin(pluginId, createTokenDto, param)
        }
    }

    /**
     * [targetMappingId] ??? ????????? [componentEntity] ??????.
     */
    private fun getMappingComponent(createTokenDto: WfTokenDto, targetMappingId: String): WfComponentEntity? {
        var componentEntity: WfComponentEntity? = null
        if (targetMappingId.isNotEmpty()) {
            val componentIds: MutableList<String> = mutableListOf()
            createTokenDto.data?.forEach { componentIds.add(it.componentId) }
            componentEntity =
                wfTokenManagerService.getComponentIdInAndMappingId(componentIds, targetMappingId)
        }
        return componentEntity
    }

    /**
     * Component ??? CI ?????? ??????
     */
    private fun getCIComponents(createTokenDto: WfTokenDto): List<WfComponentEntity> {
        val componentIds = createTokenDto.data?.map { it.componentId } ?: emptyList()
        return wfTokenManagerService.getComponentList(componentIds.toSet(), WfComponentConstants.ComponentType.CI.code)
    }

    /**
     * 1. [element.elementScriptDataEntities] ?????? scriptValue ?????? ?????? ?????? (?????? ??????)
     * 2. scriptValue ????????? scriptActionList(?????? ??????), targetMappingId, sourceMappingId ??????
     * 3. ?????? ????????? ?????? ???????????? ???????????? ?????? ??????
     *   3-1) sourceMappingId (O) + ????????? (O) -> sourceMappingId ???????????? value ?????? ???????????? ??????
     *   3-2) sourceMappingId (O) + ????????? (X) -> ?????? ??????
     *   3-3) sourceMappingId (X) + ????????? (O) -> ?????? ??????
     *   3-4) sourceMappingId (X) + ????????? (X) -> ?????? ??????
     * 4. ???????????? ????????? ????????? ?????? ????????? ??????
     */
    private fun setDocumentAttachFile(createTokenDto: WfTokenDto, element: WfElementEntity) {
        val scriptList = this.getScriptList(element)
        scriptList.forEach { script ->
            val targetMappingId = script[WfElementConstants.AttributeId.TARGET_MAPPING_ID.value].toString()
            val sourceMappingId = script[WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value].toString()
            val scriptActionList = this.getScriptActionList(script)

            val componentIds: MutableList<String> = mutableListOf()
            var componentValue = ""
            createTokenDto.data?.forEach { componentIds.add(it.componentId) }
            if (sourceMappingId.isNotEmpty()) {
                val sourceComponentId =
                    wfTokenManagerService.getComponentIdInAndMappingId(componentIds, sourceMappingId).componentId
                componentValue = wfTokenManagerService.getComponentValue(
                    createTokenDto.tokenId,
                    sourceComponentId,
                    WfComponentConstants.ComponentValueType.STRING_SEPARATOR.code
                )
            }

            val attachFileNames: MutableList<String> = mutableListOf()
            scriptActionList.forEach { action ->
                attachFileNames.add(this.getAttachFileName(action, componentValue))
            }

            if (attachFileNames.isNotEmpty()) {
                val fileSeqList: MutableList<Long> = mutableListOf()
                for (attachFileName in attachFileNames) {
                    val processFilePath = wfTokenManagerService.getProcessFilePath(attachFileName)
                    val fileName = wfTokenManagerService.getRandomFilename()
                    val uploadFilePath =
                        wfTokenManagerService.getUploadFilePathWithNow(ResourceConstants.Path.UPLOAD.path, fileName)
                    try {
                        Files.copy(processFilePath, uploadFilePath, StandardCopyOption.REPLACE_EXISTING)
                        if (Files.exists(uploadFilePath)) {
                            val aliceFileLocEntity = AliceFileLocEntity(
                                fileSeq = 0,
                                uploadedLocation = uploadFilePath.parent.toString(),
                                sort = 0,
                                randomName = fileName,
                                originName = attachFileName,
                                fileSize = uploadFilePath.toFile().length(),
                                uploaded = true,
                                fileOwner = super.assigneeId
                            )
                            fileSeqList.add(wfTokenManagerService.uploadProcessFile(aliceFileLocEntity).fileSeq)
                        }
                    } catch (e: Exception) {
                        logger.error(e.message)
                    }
                }

                if (fileSeqList.isNotEmpty()) {
                    var fileSeqValue = ""
                    fileSeqList.forEachIndexed { index, fileSeq ->
                        if (index != 0) {
                            fileSeqValue += ","
                        }
                        fileSeqValue += fileSeq
                    }

                    // ?????? ???????????? ?????? seq ?????? ??????
                    val targetComponentId =
                        wfTokenManagerService.getComponentIdInAndMappingId(componentIds, targetMappingId).componentId
                    run loop@{
                        createTokenDto.data?.forEach { data ->
                            if (data.componentId == targetComponentId) {
                                data.value = fileSeqValue
                                return@loop
                            }
                        }
                    }
                    super.tokenEntity.tokenDataEntities =
                        wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
                }
            }
        }
    }

    /**
     * [script] ??????????????? action ????????? ?????? (???????????? ?????? ?????? file ??? ??????)
     */
    private fun getScriptActionList(script: Map<String, Any>): List<Map<String, Any>> {
        val scriptActionList: MutableList<Map<String, Any>> = mutableListOf()
        if (script[WfElementConstants.AttributeId.ACTION.value] != null) {
            val actionList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                script[WfElementConstants.AttributeId.ACTION.value],
                TypeFactory.defaultInstance()
                    .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
            )
            for (action in actionList) {
                val map = HashMap<String, Any>()
                val condition = action[WfElementConstants.AttributeId.CONDITION.value].toString()
                val conditionArray =
                    condition.replace("\\s+".toRegex(), "").split("(?=[a-zA-Z0-9])".toRegex(), 2)
                if (conditionArray.size == 2) {
                    map["operator"] = conditionArray[0]
                    map["value"] = conditionArray[1]
                }
                map["file"] = action[WfElementConstants.AttributeId.FILE.value].toString()
                scriptActionList.add(map)
            }
        }
        return scriptActionList
    }

    /**
     * [scriptValue] ?????? ?????? Map ????????? ?????? (Element ??? ????????? ???????????? ?????? List ??????)
     */
    private fun getScriptList(element: WfElementEntity): List<Map<String, Any>> {
        val scriptList = mutableListOf<Map<String, Any>>()
        element.elementScriptDataEntities.forEach { elementData ->
            if (!elementData.scriptValue.isNullOrEmpty()) {
                scriptList.add(
                    mapper.readValue(elementData.scriptValue, object : TypeReference<Map<String, Any>>() {})
                )
            }
        }
        return scriptList
    }

    /**
     * ???????????? ?????? (componentValue ?????? ?????? ?????? ?????? ??????)
     */
    private fun getAttachFileName(action: Map<String, Any>, componentValue: String): String {
        var attachFileName = ""
        run loop@{
            if (action["operator"] != null) {
                when (action["operator"]) {
                    WorkflowConstants.Operator.EQUAL.code -> {
                        if (componentValue == action["value"]) {
                            attachFileName = action["file"].toString()
                            return@loop
                        }
                    }
                    WorkflowConstants.Operator.LITTLE_EQUAL.code -> {
                        if (componentValue.toInt() <= (action["value"] ?: 0) as Int) {
                            attachFileName = action["file"].toString()
                            return@loop
                        }
                    }
                    WorkflowConstants.Operator.LITTLE.code -> {
                        if (componentValue.toInt() < (action["value"] ?: 0) as Int) {
                            attachFileName = action["file"].toString()
                            return@loop
                        }
                    }
                    WorkflowConstants.Operator.GREATER_EQUAL.code -> {
                        if (componentValue.toInt() >= (action["value"] ?: 0) as Int) {
                            attachFileName = action["file"].toString()
                            return@loop
                        }
                    }
                    WorkflowConstants.Operator.GREATER.code -> {
                        if (componentValue.toInt() > (action["value"] ?: 0) as Int) {
                            attachFileName = action["file"].toString()
                            return@loop
                        }
                    }
                }
            } else {
                // ?????? ?????? ????????? ???????????? ?????? ?????? ??????
                if (action["file"] != null) {
                    attachFileName = action["file"].toString()
                }
            }
        }
        return attachFileName
    }
}
