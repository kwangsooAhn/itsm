package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

abstract class WfTokenManager(val wfTokenManagerService: WfTokenManagerService) {

    lateinit var createTokenEntity: WfTokenEntity
    lateinit var assigneeId: String

    /**
     * Create token.
     */
    fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        this.assigneeId = wfTokenDto.assigneeId.toString()
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        this.createTokenEntity = wfTokenManagerService.saveToken(token)
        wfTokenDto.tokenId = this.createTokenEntity.tokenId
        return this.createElementToken(wfTokenDto)
    }

    abstract fun createElementToken(wfTokenDto: WfTokenDto): WfTokenDto

    /**
     * Create next token.
     */
    fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        when (wfTokenDto.elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                wfTokenDto.isAutoComplete = this.setAutoComplete(wfTokenDto.elementType)
            }
            else -> {
                val element = wfTokenManagerService.getNextElement(wfTokenDto)
                wfTokenDto.elementId = element.elementId
                wfTokenDto.elementType = element.elementType
                wfTokenDto.isAutoComplete = this.setAutoComplete(wfTokenDto.elementType)
            }
        }
        return this.createNextElementToken(wfTokenDto)
    }

    abstract fun createNextElementToken(wfTokenDto: WfTokenDto): WfTokenDto

    /**
     * Complete token.
     */
    fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.getToken(wfTokenDto.tokenId)
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        this.createTokenEntity = wfTokenManagerService.saveToken(token)
        wfTokenDto.tokenId = token.tokenId
        if (!token.instance.pTokenId.isNullOrEmpty()) {
            wfTokenDto.parentTokenId = token.instance.pTokenId
        }
        return this.completeElementToken(wfTokenDto)
    }

    abstract fun completeElementToken(wfTokenDto: WfTokenDto): WfTokenDto

    /**
     * Set Assignee + Candidate.
     *
     * @param token
     */
    fun setCandidate(token: WfTokenEntity) {
        val assigneeType =
            getAttributeValue(
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
                    getAttributeValues(
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
            else -> {
                token.assigneeId = this.assigneeId
                when (token.element.elementType) {
                    WfElementConstants.ElementType.MANUAL_TASK.value -> {
                        wfTokenManagerService.saveNotification(wfTokenManagerService.saveToken(token))
                    }
                    else -> wfTokenManagerService.saveToken(token)
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
            getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
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

    /**
     * Set autoComplete by elementType.
     */
    private fun setAutoComplete(elementType: String): Boolean {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value,
            WfElementConstants.ElementType.MANUAL_TASK.value,
            WfElementConstants.ElementType.SIGNAL_SEND.value,
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> true
            else -> false
        }
    }

    /**
     * Set Token Data.
     *
     * @param wfTokenDto
     * @return MutableList<WfTokenDataEntity>
     */
    fun setTokenData(wfTokenDto: WfTokenDto): MutableList<WfTokenDataEntity> {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        if (wfTokenDto.data != null) {
            for (tokenDataDto in wfTokenDto.data!!) {
                val tokenDataEntity = WfTokenDataEntity(
                    tokenId = wfTokenDto.tokenId,
                    componentId = tokenDataDto.componentId,
                    value = tokenDataDto.value
                )
                tokenDataEntities.add(tokenDataEntity)
            }
        }
        return tokenDataEntities
    }

    /**
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    fun getAttributeValue(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): String {
        var attributeValue = ""
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValue = data.attributeValue
            }
        }
        return attributeValue
    }

    /**
     * Get AttributeValues.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return MutableList<String> (attributeValue)
     */
    private fun getAttributeValues(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): MutableList<String> {
        val attributeValues: MutableList<String> = mutableListOf()
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValues.add(data.attributeValue)
            }
        }
        return attributeValues
    }
}
