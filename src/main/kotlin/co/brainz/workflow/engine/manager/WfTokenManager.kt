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
    fun createToken(tokenDto: WfTokenDto): WfTokenDto {
        this.assigneeId = tokenDto.assigneeId.toString()
        val token = wfTokenManagerService.makeTokenEntity(tokenDto)
        this.createTokenEntity = wfTokenManagerService.saveToken(token)
        val createTokenDto = tokenDto.copy()
        createTokenDto.tokenId = this.createTokenEntity.tokenId
        return this.createElementToken(createTokenDto)
    }

    /**
     * Abstract Create token (createToken + @).
     */
    abstract fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto

    /**
     * Create next token.
     */
    fun createNextToken(tokenDto: WfTokenDto): WfTokenDto {
        val createNextTokenDto = tokenDto.copy()
        return this.createNextElementToken(createNextTokenDto)
    }

    /**
     * Abstract Create next token (createNextToken + @).
     */
    abstract fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto

    /**
     * Complete token.
     */
    fun completeToken(tokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        this.createTokenEntity = wfTokenManagerService.saveToken(token)
        val completedToken = tokenDto.copy()
        completedToken.tokenId = token.tokenId
        if (!token.instance.pTokenId.isNullOrEmpty()) {
            completedToken.parentTokenId = token.instance.pTokenId
        }
        return this.completeElementToken(completedToken)
    }

    /**
     * Abstract Complete element token (completeToken + @).
     */
    abstract fun completeElementToken(completedToken: WfTokenDto): WfTokenDto

    /**
     * Action - Save.
     */
    fun actionSave(tokenDto: WfTokenDto) {
        // Save Token & Token Data
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.assigneeId = tokenDto.assigneeId
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                token = token,
                component = wfTokenManagerService.getComponent(tokenDataDto.componentId),
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            wfTokenManagerService.saveAllTokenData(tokenDataEntities)
        }
        wfTokenManagerService.saveToken(token)
    }

    /**
     * Set Assignee + Candidate.
     */
    protected fun setCandidate(token: WfTokenEntity) {
        val assigneeType =
            this.getAttributeValue(
                token.element.elementDataEntities,
                WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
            )
        when (assigneeType) {
            WfTokenConstants.AssigneeType.ASSIGNEE.code -> {
                this.setAssignee(token)
            }
            WfTokenConstants.AssigneeType.USERS.code,
            WfTokenConstants.AssigneeType.GROUPS.code -> {
                this.setAssigneeUsersAndGroups(token, assigneeType)
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
     * Set Token Data.
     */
    protected fun setTokenData(tokenDto: WfTokenDto): MutableList<WfTokenDataEntity> {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        if (tokenDto.data != null) {
            for (tokenDataDto in tokenDto.data!!) {
                val tokenDataEntity = WfTokenDataEntity(
                    token = this.createTokenEntity,
                    component = wfTokenManagerService.getComponent(tokenDataDto.componentId),
                    value = tokenDataDto.value
                )
                tokenDataEntities.add(tokenDataEntity)
            }
        }
        return tokenDataEntities
    }

    /**
     * Get AttributeValue.
     */
    protected fun getAttributeValue(
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
     * Set assignee.
     */
    private fun setAssignee(token: WfTokenEntity) {
        var assigneeId = this.getAssignee(token.element, token)
        if (assigneeId.isEmpty()) {
            assigneeId = this.assigneeId
        }
        token.assigneeId = assigneeId
        wfTokenManagerService.saveNotification(wfTokenManagerService.saveToken(token))
    }

    /**
     * Set assignee users & groups.
     */
    private fun setAssigneeUsersAndGroups(token: WfTokenEntity, assigneeType: String) {
        val candidates =
            this.getAttributeValues(
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

    /**
     * Get Assignee.
     */
    private fun getAssignee(element: WfElementEntity, token: WfTokenEntity): String {
        val assigneeMappingId =
            this.getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
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
     * Get AttributeValues.
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

    /**
     * Set autoComplete by elementType.
     */
    protected fun setAutoComplete(elementType: String): Boolean {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value,
            WfElementConstants.ElementType.MANUAL_TASK.value,
            WfElementConstants.ElementType.SIGNAL_SEND.value,
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> true
            else -> false
        }
    }

    /**
     * Set next element info.
     */
    protected fun setNextTokenDto(createNextTokenDto: WfTokenDto): WfTokenDto {
        val element = wfTokenManagerService.getNextElement(createNextTokenDto)
        createNextTokenDto.elementId = element.elementId
        createNextTokenDto.elementType = element.elementType
        return createNextTokenDto
    }
}
