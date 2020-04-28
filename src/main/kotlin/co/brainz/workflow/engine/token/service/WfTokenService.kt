package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.constants.WfDocumentConstants
import co.brainz.workflow.engine.document.repository.WfDocumentDataRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDataDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.dto.WfTokenViewDto
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfTokenService(
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentDataRepository: WfDocumentDataRepository,
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val wfTokenElementService: WfTokenElementService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Search Tokens.
     *
     * @param parameters
     * @return List<LinkedHashMap<String, Any>>
     */
    fun getTokens(parameters: LinkedHashMap<String, Any>): List<WfTokenDto> {
        var assignee = ""
        var tokenStatus = ""
        if (parameters["assignee"] != null) {
            assignee = parameters["assignee"].toString()
        }
        if (parameters["tokenStatus"] != null) {
            tokenStatus = parameters["tokenStatus"].toString()
        }
        val tokenEntities = wfTokenRepository.findTokenMstEntityByAssigneeIdAndTokenStatus(
            assignee,
            tokenStatus
        )

        val returnValue: MutableList<WfTokenDto> = mutableListOf()

        for (tokenEntity in tokenEntities) {
            returnValue.add(
                WfTokenDto(
                    tokenId = tokenEntity.tokenId,
                    elementId = tokenEntity.element.elementId,
                    tokenStatus = tokenEntity.tokenStatus,
                    assigneeId = tokenEntity.assigneeId,
                    documentId = tokenEntity.instance.document.documentId,
                    documentName = tokenEntity.instance.document.documentName
                )
            )
        }

        return returnValue
    }

    /**
     * Token View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getToken(tokenId: String): WfTokenDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val componentList: MutableList<WfTokenDataDto> = mutableListOf()
        for (tokenDataEntity in tokenDataEntities) {
            val tokenDataDto = WfTokenDataDto(
                componentId = tokenDataEntity.componentId,
                value = tokenDataEntity.value
            )
            componentList.add(tokenDataDto)
        }

        return WfTokenDto(
            tokenId = tokenEntity.get().tokenId,
            elementId = tokenEntity.get().element.elementId,
            assigneeId = tokenEntity.get().assigneeId,
            tokenStatus = tokenEntity.get().tokenStatus,
            isComplete = tokenEntity.get().tokenStatus == WfTokenConstants.Status.FINISH.code,
            instanceId = tokenEntity.get().instance.instanceId,
            documentId = tokenEntity.get().instance.document.documentId,
            documentName = tokenEntity.get().instance.document.documentName,
            data = componentList
        )

        /*val returnValue = LinkedHashMap<String, Any>()
        returnValue["token"] = tokenDto*/

        // return tokenDto
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenData(tokenId: String): WfTokenViewDto {
        val tokenMstEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val componentEntities = tokenMstEntity.get().instance.document.form.components
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val documentDataEntities = wfDocumentDataRepository.findByDocumentIdAndElementId(
                tokenMstEntity.get().instance.document.documentId,
                tokenMstEntity.get().element.elementId
        )

        val componentList: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        if (componentEntities != null) {
            for (componentEntity in componentEntities) {
                val attributes = wfFormService.makeAttributes(componentEntity)
                val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
                for (tokenDataEntity in tokenDataEntities) {
                    if (tokenDataEntity.componentId == componentEntity.componentId) {
                        val valueMap = LinkedHashMap<String, Any>()
                        valueMap["value"] = tokenDataEntity.value
                        values.add(valueMap)
                    }
                }
                var displayType = when (tokenMstEntity.get().element.elementType) {
                    WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                        WfDocumentConstants.DisplayType.EDITABLE.value
                    }
                    else -> WfDocumentConstants.DisplayType.READONLY.value
                }
                for (documentDataEntity in documentDataEntities) {
                    if (documentDataEntity.componentId == componentEntity.componentId) {
                        displayType = documentDataEntity.display
                    }
                }

                val component = LinkedHashMap<String, Any>()
                component["componentId"] = componentEntity.componentId
                component["attributes"] = attributes
                component["values"] = values
                component["displayType"] = displayType
                componentList.add(component)
            }
        }

        val componentsMap = LinkedHashMap<String, Any>()
        componentsMap["components"] = componentList

        return WfTokenViewDto(
            tokenId = tokenMstEntity.get().tokenId,
            instanceId = tokenMstEntity.get().instance.instanceId,
            components = componentList,
            actions = wfActionService.actions(tokenMstEntity.get().element.elementId)
        )
    }

    /**
     * (POST) Init Token.
     *
     * @param wfTokenDto
     */
    fun initToken(wfTokenDto: WfTokenDto) {
        wfTokenElementService.initToken(wfTokenDto)
    }

    /**
     * (PUT) Set Token Action.
     *
     * @param wfTokenDto
     */
    fun setToken(wfTokenDto: WfTokenDto) {
        wfTokenElementService.setTokenAction(wfTokenDto)
    }

    /**
     * Get Token Assignee.
     *
     * @param assigneeData
     * @return String
     */
    private fun getAssigneeForToken(assigneeData: String?): String {
        // 데이터가 만약 mappingExpresion('${xxxx}'와 같은) 이라면 문서에서 해당 코드로 매핑된 데이터를 찾아서 사용.
        // 데이터가 단순히 문자열이라면 그대로 사용.
        var assigneeForToken = ""

        val mappingExpr = WfTokenConstants.mappingExpression.toRegex()
        assigneeData?.let {
            assigneeForToken =
                if (mappingExpr.matches(assigneeData)) it.substring(2, assigneeData.length - 1) else assigneeData
        }
        return assigneeForToken
    }
}
