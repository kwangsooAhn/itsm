package co.brainz.workflow.token.service

import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.dto.*
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
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
    private val wfCandidateRepository: WfCandidateRepository
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
    fun getToken(tokenId: String): RestTemplateTokenDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId).get()
        val componentList: MutableList<RestTemplateTokenDataDto> = mutableListOf()
        tokenEntity.tokenDataEntities.forEach { data ->
            val tokenDataDto = RestTemplateTokenDataDto(
                componentId = data.component.componentId,
                value = data.value
            )
            componentList.add(tokenDataDto)
        }

        return RestTemplateTokenDto(
            tokenId = tokenEntity.tokenId,
            elementId = tokenEntity.element.elementId,
            assigneeId = tokenEntity.assigneeId,
            tokenStatus = tokenEntity.tokenStatus,
            isComplete = tokenEntity.tokenStatus == WfTokenConstants.Status.FINISH.code,
            instanceId = tokenEntity.instance.instanceId,
            documentId = tokenEntity.instance.document.documentId,
            documentName = tokenEntity.instance.document.documentName,
            data = componentList
        )
    }

    /**
     * Token에 대한 ElementData.
     *
     * @param tokenId
     * @return List<RestTemplateTokenElementDataViewDto>
     */
    fun getTokenElementData(tokenId: String): RestTemplateTokenElementDataViewDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId).get()
        val attributeData = mutableListOf<LinkedHashMap<String, String>>()
        tokenEntity.element.elementDataEntities.forEach { elementData ->
            val elementDataMap = LinkedHashMap<String, String>()
            elementDataMap["attributeId"] = elementData.attributeId
            elementDataMap["attributeValue"] = elementData.attributeValue
            attributeData.add(elementDataMap)
        }

        return RestTemplateTokenElementDataViewDto(
            tokenId = tokenEntity.tokenId,
            elementId = tokenEntity.element.elementId,
            attributeData = attributeData
        )
    }

    /**
     * Token에 대한 Assignees.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenAssignees(tokenId: String): RestTemplateTokenAssigneesViewDto {
        var type = ""
        val assignees = mutableListOf<String>()
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId).get()
        tokenEntity.element.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == "assignee-type") {
                type = elementData.attributeValue
            }
        }

        if (type == WfTokenConstants.AssigneeType.ASSIGNEE.code) {
            assignees.add(tokenEntity.assigneeId.toString())
        } else if (type == WfTokenConstants.AssigneeType.USERS.code || type == WfTokenConstants.AssigneeType.GROUPS.code) {
            wfCandidateRepository.findByTokenIdAndCandidateType(tokenEntity, type).forEach { candidate ->
                assignees.add(candidate.candidateValue)
            }
        }

        return RestTemplateTokenAssigneesViewDto(
            tokenId = tokenId,
            type = type,
            assignees = assignees
        )
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenData(tokenId: String): RestTemplateTokenViewDto {
        // FormComponent
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val formId = tokenEntity.get().instance.document.form.formId
        val formData = wfFormService.getFormComponentList(formId)

        val documentDisplayList =
            wfDocumentDisplayRepository.findByDocumentIdAndElementId(
                tokenEntity.get().instance.document.documentId,
                tokenEntity.get().element.elementId
            )

        for (componentEntity in formData.components) {
            // value
            val tokenDataEntities = wfTokenDataRepository.findWfTokenDataEntitiesByTokenTokenId(tokenId)
            for (tokenDataEntity in tokenDataEntities) {
                if (tokenDataEntity.component.componentId == componentEntity.componentId) {
                    componentEntity.value = tokenDataEntity.value
                }
            }

            // displayType
            for (documentDisplay in documentDisplayList) {
                if (componentEntity.componentId == documentDisplay.componentId) {
                    componentEntity.dataAttribute["displayType"] = documentDisplay.display
                }
            }
        }

        return RestTemplateTokenViewDto(
            tokenId = tokenId,
            instanceId = tokenEntity.get().instance.instanceId,
            form = formData,
            actions = wfActionService.actions(tokenEntity.get().element.elementId)
        )
    }
}
