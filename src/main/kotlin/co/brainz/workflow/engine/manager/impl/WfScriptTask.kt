package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import java.io.File

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
        val scriptValueList: MutableList<String> = mutableListOf()
        element.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == WfElementConstants.AttributeId.TARGET_MAPPING_ID.value) {
                targetMappingId = elementData.attributeValue
            }
            if (elementData.attributeId == WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value) {
                sourceMappingId = elementData.attributeValue
            }
            // 테이블로 변경시 수정 필요
            if (elementData.attributeId == WfElementConstants.AttributeId.SCRIPT_VALUE.value) {
                scriptValueList.add(elementData.attributeValue)
            }
        }

        val componentIds: MutableList<String> = mutableListOf()
        createTokenDto.data?.forEach { componentIds.add(it.componentId) }
        val sourceComponentId =
            wfTokenManagerService.getComponentIdInAndMappingId(componentIds, sourceMappingId).componentId
        val componentValue = wfTokenManagerService.getComponentValue(createTokenDto.tokenId, sourceComponentId)

        var attachFileName = ""
        scriptValueList.forEach { scriptValue ->
            val scriptValueArray = scriptValue.split("|")
            val conditionArray = scriptValueArray[0].replace("\\s+".toRegex(), "").split("(?=[a-zA-Z0-9])".toRegex(), 2)
            if (conditionArray.size == 2) {
                when (conditionArray[0]) {
                    "=" -> {
                        if (componentValue == conditionArray[1]) {
                            attachFileName = scriptValueArray[1]
                            return@forEach
                        }
                    }
                    "<=" -> {
                        if (componentValue.toInt() <= conditionArray[1].toInt()) {
                            attachFileName = scriptValueArray[1]
                            return@forEach
                        }
                    }
                    "<" -> {
                        if (componentValue.toInt() < conditionArray[1].toInt()) {
                            attachFileName = scriptValueArray[1]
                            return@forEach
                        }
                    }
                    ">=" -> {
                        if (componentValue.toInt() >= conditionArray[1].toInt()) {
                            attachFileName = scriptValueArray[1]
                            return@forEach
                        }
                    }
                    ">" -> {
                        if (componentValue.toInt() > conditionArray[1].toInt()) {
                            attachFileName = scriptValueArray[1]
                            return@forEach
                        }
                    }
                }
            }
        }

        if (attachFileName.isNotEmpty()) {
            val targetComponentId =
                wfTokenManagerService.getComponentIdInAndMappingId(componentIds, targetMappingId).componentId
            createTokenDto.data?.forEach { data ->
                if (data.componentId == targetComponentId) {
                    data.value = WfElementConstants.SCRIPT_FILE_PATH + File.separator + attachFileName
                    return@forEach
                }
            }
            super.tokenEntity.tokenDataEntities =
                wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
        }
    }
}
