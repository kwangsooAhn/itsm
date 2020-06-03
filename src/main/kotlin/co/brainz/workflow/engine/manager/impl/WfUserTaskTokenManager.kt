package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

class WfUserTaskTokenManager(
    wfTokenManagerService: WfTokenManagerService
) : WfTokenManager(wfTokenManagerService) {

    lateinit var assigneeId: String

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        val saveToken = wfTokenManagerService.saveToken(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType

        saveToken.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
        this.assigneeId = wfTokenDto.assigneeId.toString()
        setCandidate(saveToken)

        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.getToken(wfTokenDto.tokenId)
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        wfTokenManagerService.saveToken(token)
        if (!token.instance.pTokenId.isNullOrEmpty()) {
            wfTokenDto.parentTokenId = token.instance.pTokenId
        }

        token.tokenData = wfTokenManagerService.saveAllTokenData(super.setTokenData(wfTokenDto))
        token.assigneeId = wfTokenDto.assigneeId
        wfTokenManagerService.saveToken(token)

        return wfTokenDto
    }

    /**
     * Set Assignee + Candidate.
     *
     */
    private fun setCandidate(token: WfTokenEntity) {
        val assigneeType =
            super.getAttributeValue(
                token.element.elementDataEntities,
                WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
            )
        when (assigneeType) {
            WfTokenConstants.AssigneeType.ASSIGNEE.code -> {
                var assigneeId = getAssignee(token.element, token)
                if (assigneeId.isEmpty()) {
                    assigneeId = this.assigneeId
                }
                token.assigneeId = assigneeId
                wfTokenManagerService.saveNotification(wfTokenManagerService.saveToken(token))
            }
            WfTokenConstants.AssigneeType.USERS.code,
            WfTokenConstants.AssigneeType.GROUPS.code -> {
                val candidates =
                    super.getAttributeValues(
                        token.element.elementDataEntities,
                        WfElementConstants.AttributeId.ASSIGNEE.value
                    )
                if (candidates.isNotEmpty()) {
                    val wfCandidateEntities = mutableListOf<WfCandidateEntity>()
                    candidates.forEach { candidate ->
                        val wfCandidateEntity = WfCandidateEntity(
                            token = token,
                            candidateType = assigneeType,
                            candidateValue = candidate
                        )
                        wfCandidateEntities.add(wfCandidateEntity)
                    }
                    wfTokenManagerService.saveNotification(
                        token,
                        wfTokenManagerService.saveAllCandidate(wfCandidateEntities)
                    )
                } else {
                    token.assigneeId = this.assigneeId
                    wfTokenManagerService.saveNotification(wfTokenManagerService.saveToken(token))
                }
            }
        }
    }

    /**
     * Get Assignee.
     *
     * @param element
     * @param token
     * @return String
     */
    private fun getAssignee(element: WfElementEntity, token: WfTokenEntity): String {
        val assigneeMappingId =
            super.getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
        var componentMappingId = ""
        token.instance.document.form.components?.forEach { component ->
            if (component.mappingId.isNotEmpty() && component.mappingId == assigneeMappingId) {
                componentMappingId = component.componentId
            }
        }
        var assignee = ""
        if (componentMappingId.isNotEmpty()) {
            assignee = wfTokenManagerService.getComponentValue(token.tokenId, componentMappingId)
        }
        return assignee
    }
}
