package co.brainz.workflow.token.service

import co.brainz.itsm.instance.constants.InstanceConstants
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.provider.dto.RestTemplateTokenStakeholderViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenViewDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.LinkedMultiValueMap
import java.util.Optional
import kotlin.collections.LinkedHashMap

@Service
@Transactional
class WfTokenService(
    private val instanceService: InstanceService,
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

        val tokenData = LinkedMultiValueMap<String, String>()
        tokenData["tokenId"] = tokenEntity.get().tokenId
        tokenData["status"] = tokenEntity.get().tokenStatus

        return RestTemplateTokenViewDto(
            token = tokenData,
            instanceId = tokenEntity.get().instance.instanceId,
            form = formData,
            actions = wfActionService.actions(tokenEntity.get().element.elementId),
            stakeholders = this.getTokenStakeholders(tokenEntity)
        )
    }
    
    /**
     * Token에 대한 문서관련자
     *
     * @param tokenEntity
     * @return RestTemplateTokenStakeholderViewDto
     */
    fun getTokenStakeholders(tokenEntity: Optional<WfTokenEntity>): RestTemplateTokenStakeholderViewDto {
        var assigneeType = ""
        val assignees = mutableListOf<String>()
        tokenEntity.get().element.elementDataEntities.forEach { elementData ->
            if (elementData.attributeId == "assignee-type") {
                assigneeType = elementData.attributeValue
            }
        }

        if (assigneeType == WfTokenConstants.AssigneeType.ASSIGNEE.code) {
            assignees.add(tokenEntity.get().assigneeId.toString())
        } else if (assigneeType == WfTokenConstants.AssigneeType.USERS.code ||
            assigneeType == WfTokenConstants.AssigneeType.GROUPS.code
        ) {
            wfCandidateRepository.findByTokenIdAndCandidateType(tokenEntity, assigneeType).forEach { candidate ->
                assignees.add(candidate.candidateValue)
            }
        }

        return RestTemplateTokenStakeholderViewDto(
            type = assigneeType,
            assignee = assignees,
            revokeAssignee = this.getRevokeAssignee(tokenEntity.get().tokenId)
        )
    }

    /**
     * tokenId를 통해서 이전 UserTask 담당자Id를 반환 한다.
     *
     * @param tokenId
     * @return String
     */
    fun getRevokeAssignee(tokenId: String): String {
        var assigneeId = ""
        var isBreak = true
        instanceService.getInstanceHistory(tokenId)?.sortedBy { it.tokenEndDt }?.reversed()
            ?.forEach { element ->
                if (element.tokenEndDt != null &&
                    element.elementType == InstanceConstants.ElementListForHistoryViewing.USER_TASK.value &&
                    isBreak
                ) {
                    assigneeId = element.assigneeId.toString()
                    isBreak = false
                }
            }
        return assigneeId
    }
}
