/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 */

package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * 프로세스 진행간 생성되는 모든 토큰은 어떤 엘리먼트냐에 따라 동작이 달라진다.
 * 이때 각 엘리먼트 종류별로 동작을 구현하는 TokenManager들의 가장 상위 클래스.
 */
abstract class WfTokenManager(val wfTokenManagerService: WfTokenManagerService) {

    lateinit var tokenEntity: WfTokenEntity
    lateinit var assigneeId: String
    abstract var isAutoComplete: Boolean

    /**
     * Create token.
     */
    fun createToken(tokenDto: WfTokenDto): WfTokenDto {
        this.assigneeId = tokenDto.assigneeId.toString()

        val token = wfTokenManagerService.makeTokenEntity(tokenDto)
        this.tokenEntity = wfTokenManagerService.saveToken(token)

        val createTokenDto = tokenDto.copy()
        createTokenDto.tokenId = this.tokenEntity.tokenId

        // TODO : Refactoring 필요.
        // 각 tokenmanager들의 createElementToken함수에서 super.cretateTokenEntity에 뭔짓을 하는지 몰라서
        // 일단 모든 처리가 끝난 이후에 알림을 체크하기 위해서 아래와 같이 임시로 newTokenDto로 구성.
        // entity와 dto를 이중으로 관리하는 패턴이 좀 이상하다.
        val newTokenDto = this.createElementToken(createTokenDto)
        wfTokenManagerService.notificationCheck(tokenEntity)
        return newTokenDto
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
        token.tokenStatus = WfTokenConstants.Status.FINISH.code
        token.assigneeId = tokenDto.assigneeId

        this.tokenEntity = wfTokenManagerService.saveToken(token)

        val completedTokenDto = tokenDto.copy()
        completedTokenDto.tokenId = token.tokenId
        completedTokenDto.assigneeId = this.tokenEntity.assigneeId
        if (!token.instance.pTokenId.isNullOrEmpty()) {
            completedTokenDto.parentTokenId = token.instance.pTokenId
        }

        return this.completeElementToken(completedTokenDto)
    }

    /**
     * Abstract Complete element token (completeToken + @).
     */
    abstract fun completeElementToken(completedToken: WfTokenDto): WfTokenDto

    /**
     * 토큰을 대기(Waiting) 상태로 변환. (예:서브프로세스)
     */
    protected fun suspendToken(tokenDto: WfTokenDto) {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.tokenStatus = WfTokenConstants.Status.WAITING.code
        this.tokenEntity = wfTokenManagerService.saveToken(token)
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
                wfTokenManagerService.saveToken(token)
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
                    token = this.tokenEntity,
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
        wfTokenManagerService.saveToken(token)
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
            wfTokenManagerService.saveAllCandidate(wfCandidateEntities)
            token.candidate = wfCandidateEntities
        } else {
            token.assigneeId = this.assigneeId
            wfTokenManagerService.saveToken(token)
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
     * Set next element info.
     */
    protected fun setNextTokenDto(createNextTokenDto: WfTokenDto): WfTokenDto {
        val element = wfTokenManagerService.getNextElement(createNextTokenDto)
        createNextTokenDto.elementId = element.elementId
        createNextTokenDto.elementType = element.elementType
        return createNextTokenDto
    }
}
