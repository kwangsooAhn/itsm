/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager

import co.brainz.itsm.user.constants.UserConstants
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime

/**
 * 프로세스 진행간 생성되는 모든 토큰은 어떤 엘리먼트냐에 따라 동작이 달라진다.
 * 이때 각 엘리먼트 종류별로 동작을 구현하는 TokenManager들의 가장 상위 클래스.
 */
abstract class WfTokenManager(val wfTokenManagerService: WfTokenManagerService) {

    lateinit var tokenEntity: WfTokenEntity
    lateinit var assigneeId: String
    abstract var isAutoComplete: Boolean

    /**
     * 기본 토큰 생성
     *  - 토큰 엔티티 생성 및 저장.
     *  - 엘리먼트 별 토큰 생성시 처리 (createElementToken 호출)
     *  - 알람 발송
     */
    fun createToken(newTokenDto: WfTokenDto): WfTokenDto {
        this.assigneeId = newTokenDto.assigneeId.toString()
        this.tokenEntity = wfTokenManagerService.saveToken(newTokenDto)
        newTokenDto.tokenId = this.tokenEntity.tokenId

        this.tokenEntity.tokenDataEntities =
            wfTokenManagerService.saveAllTokenData(this.setTokenData(newTokenDto))
        this.setCandidate(this.tokenEntity)
        this.tokenEntity.assigneeId?.let {
            newTokenDto.assigneeId = it
        }

        this.createElementToken(newTokenDto)
        wfTokenManagerService.notificationCheck(tokenEntity)
        return newTokenDto
    }

    /**
     * 토큰 생성시 엘리먼트에 따른 처리 내용.
     *  - 추상 메소드로 상속 후 엘리먼트 별 처리 구현.
     */
    abstract fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto

    /**
     * 다음 엘리먼트의 토큰 생성.
     *  - 기본적으로 현재 토큰을 복사해서 엘리먼트 별 사전 작업 후 반환
     */
    fun createNextToken(tokenDto: WfTokenDto): WfTokenDto? {
        val createNextTokenDto = tokenDto.copy()
        return this.createNextElementToken(createNextTokenDto)
    }

    /**
     * 다음 엘리먼트의 토큰 생성 시 엘리먼트에 따른 처리 내용.
     *  - 추상 메소드로 상속 후 엘리먼트 별 처리 구현
     */
    abstract fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto?

    /**
     * 토큰 완료 처리.
     *  - 공통적인 토큰 완료 처리 및 엘리먼트 별 완료 작업.
     */
    fun completeToken(tokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.tokenEndDt = LocalDateTime.now()
        token.tokenStatus = WfTokenConstants.Status.FINISH.code
        token.tokenAction = WfTokenConstants.FinishAction.FINISH.code
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
    private fun setCandidate(token: WfTokenEntity) {
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
            WfTokenConstants.AssigneeType.GROUPS.code -> {}

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
     * Get Assignee.
     */
    private fun getAssignee(element: WfElementEntity, token: WfTokenEntity): String {
        val assigneeMappingId =
            this.getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
        var componentMappingId = ""
        var componentMappingType = ""
        token.instance.document.form.components.forEach { component ->
            if (component.mappingId.isNotEmpty() && component.mappingId == assigneeMappingId) {
                componentMappingType = component.componentType
                componentMappingId = component.componentId
            }
        }

        var assignee = ""
        if (componentMappingId.isNotEmpty()) {
            val componentValueType =
                when (componentMappingType) {
                    WfComponentConstants.ComponentTypeCode.CUSTOM_CODE.code -> {
                        WfComponentConstants.ComponentValueType.STRING_SEPARATOR.code
                    }
                    WfComponentConstants.ComponentTypeCode.USER_SEARCH.code -> {
                        WfComponentConstants.ComponentValueType.STRING_SEPARATOR.code
                    }
                    else -> WfComponentConstants.ComponentValueType.STRING.code
                }
            assignee = wfTokenManagerService.getComponentValue(
                token.tokenId,
                componentMappingId,
                componentValueType
            )
        }

        // 위임자 설정 체크
        assignee = this.getAssigneeAbsence(assignee)

        return assignee
    }

    /**
     * Change assignee to absence.
     */
    private fun getAssigneeAbsence(assignee: String): String {
        var assigneeAbsence = assignee
        if (assignee.isNotEmpty()) {
            val userEntity = wfTokenManagerService.getUserInfo(assignee)
            if (userEntity != null && userEntity.absenceYn) {
                userEntity.userCustomEntities.forEach { custom ->
                    if (custom.customType == UserConstants.UserCustom.USER_ABSENCE.code) {
                        val absenceInfo = wfTokenManagerService.getAbsenceInfo(custom)
                        val now = LocalDateTime.now()
                        if (absenceInfo.startDt!! <= now && absenceInfo.endDt!! >= now) {
                            assigneeAbsence = absenceInfo.substituteUserKey!!
                        }
                    }
                }
            }
        }
        return assigneeAbsence
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
