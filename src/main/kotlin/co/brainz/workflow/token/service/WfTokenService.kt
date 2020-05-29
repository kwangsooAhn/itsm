package co.brainz.workflow.token.service

import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentDisplayEntity
import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenViewDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfTokenService(
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentDisplayRepository: WfDocumentDisplayRepository,
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val wfTokenElementService: WfTokenElementService,
    private val wfEngine: WfEngine
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Search Tokens.
     *
     * @param parameters
     * @return List<LinkedHashMap<String, Any>>
     */
    fun getTokens(parameters: LinkedHashMap<String, Any>): List<RestTemplateTokenDto> {
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

        val returnValue: MutableList<RestTemplateTokenDto> = mutableListOf()

        for (tokenEntity in tokenEntities) {
            returnValue.add(
                RestTemplateTokenDto(
                    tokenId = tokenEntity.tokenId,
                    elementId = tokenEntity.element.elementId,
                    tokenStatus = tokenEntity.tokenStatus,
                    assigneeId = tokenEntity.assigneeId,
                    documentId = tokenEntity.instance.document!!.documentId,
                    documentName = tokenEntity.instance.document!!.documentName
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
    fun getToken(tokenId: String): RestTemplateTokenDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val componentList: MutableList<RestTemplateTokenDataDto> = mutableListOf()
        for (tokenDataEntity in tokenDataEntities) {
            val tokenDataDto = RestTemplateTokenDataDto(
                componentId = tokenDataEntity.componentId,
                value = tokenDataEntity.value
            )
            componentList.add(tokenDataDto)
        }

        return RestTemplateTokenDto(
            tokenId = tokenEntity.get().tokenId,
            elementId = tokenEntity.get().element.elementId,
            assigneeId = tokenEntity.get().assigneeId,
            tokenStatus = tokenEntity.get().tokenStatus,
            isComplete = tokenEntity.get().tokenStatus == WfTokenConstants.Status.FINISH.code,
            instanceId = tokenEntity.get().instance.instanceId,
            documentId = tokenEntity.get().instance.document!!.documentId,
            documentName = tokenEntity.get().instance.document!!.documentName,
            data = componentList
        )
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenData(tokenId: String): RestTemplateTokenViewDto {
        val tokenMstEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val componentEntities = tokenMstEntity.get().instance.document!!.form.components
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        var documentDisplayEntities: List<WfDocumentDisplayEntity> = mutableListOf()
        when (tokenMstEntity.get().element.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                val startArrow = wfActionService.getArrowElements(tokenMstEntity.get().element.elementId)[0]
                val nextElementId = wfActionService.getNextElementId(startArrow)
                when (wfActionService.getElement(nextElementId).elementType) {
                    WfElementConstants.ElementType.USER_TASK.value -> {
                        documentDisplayEntities =
                            wfDocumentDisplayRepository.findByDocumentIdAndElementId(
                                tokenMstEntity.get().instance.document!!.documentId,
                                nextElementId
                            )
                    }
                }
            }
            else -> {
                documentDisplayEntities = wfDocumentDisplayRepository.findByDocumentIdAndElementId(
                    tokenMstEntity.get().instance.document!!.documentId,
                    tokenMstEntity.get().element.elementId
                )
            }
        }

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
                var displayType = WfDocumentConstants.DisplayType.READONLY.value
                for (documentDisplayEntity in documentDisplayEntities) {
                    if (documentDisplayEntity.componentId == componentEntity.componentId) {
                        displayType = documentDisplayEntity.display
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

        return RestTemplateTokenViewDto(
            tokenId = tokenMstEntity.get().tokenId,
            instanceId = tokenMstEntity.get().instance.instanceId,
            components = componentList,
            actions = wfActionService.actions(tokenMstEntity.get().element.elementId)
        )
    }

    /**
     * (POST) Init Token.
     *
     * @param restTemplateTokenDto
     */
    fun initToken(restTemplateTokenDto: RestTemplateTokenDto) {
        wfEngine.startWorkflow(restTemplateTokenDto)
        //wfTokenElementService.initToken(restTemplateTokenDto)
    }

    /**
     * (PUT) Set Token Action.
     *
     * @param restTemplateTokenDto
     */
    fun setToken(restTemplateTokenDto: RestTemplateTokenDto) {
        //wfTokenElementService.setTokenAction(restTemplateTokenDto)
        wfEngine.progressToken(wfEngine.toTokenDto(restTemplateTokenDto))
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
