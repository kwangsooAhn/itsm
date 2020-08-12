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
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class WfScriptTask(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = true
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        var scriptType = ""
        val element = wfTokenManagerService.getElement(createTokenDto.elementId)
        element.elementDataEntities.forEach { data ->
            if (data.attributeId == WfElementConstants.AttributeId.SCRIPT_TYPE.value) {
                scriptType = data.attributeValue
                return@forEach
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
        element.elementScriptDataEntities.forEach { scriptData ->
            if (!scriptData.scriptValue.isNullOrEmpty()) {
                val scriptObject = Gson().fromJson(scriptData.scriptValue, JsonObject::class.java)
                targetMappingId = scriptObject.get(WfElementConstants.AttributeId.TARGET_MAPPING_ID.value).asString
                sourceMappingId = scriptObject.get(WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value).asString
                val actionArray = scriptObject.get(WfElementConstants.AttributeId.ACTION.value).asJsonArray
                actionArray.forEach { action ->
                    val map = HashMap<String, Any>()
                    val condition = action.asJsonObject.get(WfElementConstants.AttributeId.CONDITION.value).asString
                    val conditionArray = condition.replace("\\s+".toRegex(), "").split("(?=[a-zA-Z0-9])".toRegex(), 2)
                    if (conditionArray.size == 2) {
                        map["operator"] = conditionArray[0]
                        map["value"] = conditionArray[1]
                    }
                    map["file"] = action.asJsonObject.get(WfElementConstants.AttributeId.FILE.value).asString
                    scriptActionList.add(map)
                }
            }
        }

        val componentIds: MutableList<String> = mutableListOf()
        createTokenDto.data?.forEach { componentIds.add(it.componentId) }
        val sourceComponentId =
            wfTokenManagerService.getComponentIdInAndMappingId(componentIds, sourceMappingId).componentId
        val componentValue = wfTokenManagerService.getComponentValue(createTokenDto.tokenId, sourceComponentId)

        var attachFileName = ""
        scriptActionList.forEach { action ->
            when (action["operator"]) {
                "=" -> {
                    if (componentValue == action["value"]) {
                        attachFileName = (action["file"] ?: "") as String
                        return@forEach
                    }
                }
                "<=" -> {
                    if (componentValue.toInt() <= (action["value"] ?: 0) as Int) {
                        attachFileName = (action["file"] ?: "") as String
                        return@forEach
                    }
                }
                "<" -> {
                    if (componentValue.toInt() < (action["value"] ?: 0) as Int) {
                        attachFileName = (action["file"] ?: "") as String
                        return@forEach
                    }
                }
                ">=" -> {
                    if (componentValue.toInt() >= (action["value"] ?: 0) as Int) {
                        attachFileName = (action["file"] ?: "") as String
                        return@forEach
                    }
                }
                ">" -> {
                    if (componentValue.toInt() > (action["value"] ?: 0) as Int) {
                        attachFileName = (action["file"] ?: "") as String
                        return@forEach
                    }
                }
            }
        }

        if (attachFileName.isNotEmpty()) {
            val processFilePath = wfTokenManagerService.getProcessFilePath(attachFileName)
            val fileName = wfTokenManagerService.getRandomFilename()
            val uploadFilePath = wfTokenManagerService.getUploadFilePath(fileName)
            Files.copy(processFilePath, uploadFilePath, StandardCopyOption.REPLACE_EXISTING)
            if (Files.exists(uploadFilePath)) {
                var aliceFileLocEntity = AliceFileLocEntity(
                    fileSeq = 0,
                    uploadedLocation = uploadFilePath.parent.toString(),
                    sort = 0,
                    randomName = fileName,
                    originName = attachFileName,
                    fileSize = uploadFilePath.toFile().length(),
                    uploaded = true,
                    fileOwner = super.assigneeId
                )
                aliceFileLocEntity = wfTokenManagerService.uploadProcessFile(aliceFileLocEntity)

                // 토큰 데이터에 파일 seq 값을 저장
                val targetComponentId =
                    wfTokenManagerService.getComponentIdInAndMappingId(componentIds, targetMappingId).componentId
                createTokenDto.data?.forEach { data ->
                    if (data.componentId == targetComponentId) {
                        data.value = aliceFileLocEntity.fileSeq.toString()
                        return@forEach
                    }
                }
                super.tokenEntity.tokenDataEntities =
                    wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
            }
        }
    }
}
