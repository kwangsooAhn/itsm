/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.impl

import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.StandardCopyOption


class WfScriptTask(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = true
) : WfTokenManager(wfTokenManagerService) {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

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

        when (scriptType) {
            WfElementConstants.ScriptType.DOCUMENT_ATTACH_FILE.value ->
                this.setDocumentAttachFile(createTokenDto, element)
        }

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        return completedToken
    }

    /**
     * 1. [targetMappingId] 값으로 조건이 되는 컴포넌트를 찾고
     * 2. 값을 비교하여 찾은 파일을
     * 3. [sourceMappingId] 값으로 매핑된 컴포넌트에 추가한다.
     */
    private fun setDocumentAttachFile(createTokenDto: WfTokenDto, element: WfElementEntity) {
        var targetMappingId = ""
        var sourceMappingId = ""
        val scriptActionList: MutableList<Map<String, Any>> = mutableListOf()
        for (scriptData in element.elementScriptDataEntities) {
            if (!scriptData.scriptValue.isNullOrEmpty()) {
                val scriptMap: Map<String, Any> =
                    mapper.readValue(scriptData.scriptValue, object : TypeReference<Map<String, Any>>() {})
                targetMappingId = scriptMap[WfElementConstants.AttributeId.TARGET_MAPPING_ID.value].toString()
                sourceMappingId = scriptMap[WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value].toString()
                if (scriptMap[WfElementConstants.AttributeId.ACTION.value] != null) {
                    val actionList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                        scriptMap[WfElementConstants.AttributeId.ACTION.value],
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
            }
        }

        val componentIds: MutableList<String> = mutableListOf()
        createTokenDto.data?.forEach { componentIds.add(it.componentId) }
        val sourceComponentId =
            wfTokenManagerService.getComponentIdInAndMappingId(componentIds, sourceMappingId).componentId
        val componentValue = wfTokenManagerService.getComponentValue(createTokenDto.tokenId, sourceComponentId)
        val attachFileNames: MutableList<String> = mutableListOf()
        scriptActionList.forEach { action ->
            when (action["operator"]) {
                "=" -> {
                    if (componentValue == action["value"]) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                "<=" -> {
                    if (componentValue.toInt() <= (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                "<" -> {
                    if (componentValue.toInt() < (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                ">=" -> {
                    if (componentValue.toInt() >= (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                ">" -> {
                    if (componentValue.toInt() > (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
            }
        }

        if (attachFileNames.isNotEmpty()) {
            val fileSeqList: MutableList<Long> = mutableListOf()
            for (attachFileName in attachFileNames) {
                val processFilePath = wfTokenManagerService.getProcessFilePath(attachFileName)
                val fileName = wfTokenManagerService.getRandomFilename()
                val uploadFilePath = wfTokenManagerService.getUploadFilePath(fileName)
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
            }

            if (fileSeqList.isNotEmpty()) {
                var fileSeqValue = ""
                fileSeqList.forEachIndexed { index, fileSeq ->
                    if (index != 0) {
                        fileSeqValue += ","
                    }
                    fileSeqValue += fileSeq
                }

                // 토큰 데이터에 파일 seq 값을 저장
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
