/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId

class WfTokenAction(
    private val wfTokenManagerService: WfTokenManagerService
) {

    fun action(tokenDto: WfTokenDto) {
        when (tokenDto.action) {
            WfElementConstants.Action.SAVE.value -> this.actionSave(tokenDto)
            WfElementConstants.Action.CANCEL.value -> this.actionCancel(tokenDto)
            WfElementConstants.Action.TERMINATE.value -> this.actionTerminate(tokenDto)
            WfElementConstants.Action.WITHDRAW.value -> this.actionWithdraw(tokenDto)
            WfElementConstants.Action.REJECT.value -> this.actionReject(tokenDto)
        }
    }

    /**
     * Save.
     */
    private fun actionSave(tokenDto: WfTokenDto) {
        this.updateToken(tokenDto)
    }

    /**
     * Cancel.
     */
    private fun actionCancel(tokenDto: WfTokenDto) {
        val token = this.updateToken(tokenDto)
        wfTokenManagerService.completeInstance(token.instance.instanceId)
    }

    /**
     * Terminate.
     */
    private fun actionTerminate(tokenDto: WfTokenDto) {
        val token = this.updateToken(tokenDto)

        // end token
        val element = wfTokenManagerService.getEndElement(token.instance.document.process.processId)
        val commonEndTokenDto = WfTokenDto(
            tokenId = "",
            tokenStatus = WfTokenConstants.Status.FINISH.code,
            documentId = token.instance.document.documentId,
            instanceId = token.instance.instanceId,
            elementId = element.elementId,
            elementType = element.elementType
        )
        val tokenManager =
            WfTokenManagerFactory(wfTokenManagerService).createTokenManager(commonEndTokenDto.elementType)
        WfEngine(wfTokenManagerService).progressWorkflow(tokenManager.createToken(commonEndTokenDto))
    }

    /**
     * Action - 반려
     *
     * 현재 토큰의 상태를 반려로 업데이트하고 반려할 대상 엘리먼트를 찾아서 신규 토큰을 생성한다
     * 파라미터 [tokenDto] 에 tokenId 만 넘어오므로 현재 토큰 추가 조회가 필요하다
     */
    private fun actionReject(tokenDto: WfTokenDto) {
        // 현재 토큰을 조회하고 상태를 reject 로 업데이트
        val currentToken = this.updateToken(tokenDto)

        // 현재 엘리먼트 데이터의 reject-id 를 확인하여 반려할 엘리먼트를 조회한다.
        val elementIdToReject = currentToken.element.getElementDataValue(WfElementConstants.AttributeId.REJECT_ID.value)
            ?: throw AliceException(AliceErrorConstants.ERR_00005, "Not found reject element data. check reject-id.")

        // 인스턴스의 토큰 이력들 중 반려할 엘리먼트의 마지막 토큰 정보를 리턴한다.(반려가 여러번일 수 있음)
        val tokenToReject = currentToken.instance.tokens!!.filter {
            it.element.elementId == elementIdToReject
        }.maxWith(Comparator { token1, token2 ->
            when {
                token1.tokenStartDt!! > token2.tokenStartDt -> 1
                token1.tokenStartDt == token2.tokenStartDt -> 0
                else -> -1
            }
        }) ?: throw AliceException(AliceErrorConstants.ERR_00005, "Not found reject element in tokens.")

        this.createToken(tokenDto.copy(), tokenToReject)
    }

    /**
     * Action - 회수
     *
     * [tokenDto] 의 tokenId 만 넘어오므로 현재 토큰 추가 조회가 필요함
     */
    private fun actionWithdraw(tokenDto: WfTokenDto) {
        // 현재 토큰과 엘리먼트를 조회하고 상태를 widthdraw 로 업데이트한다.
        // assignee 은 업데이트를 생략하기 위해 null 을 채운다.
        val updateTokenDto = tokenDto.copy()
        updateTokenDto.assigneeId = null
        val currentToken = this.updateToken(updateTokenDto)

        // 현재 토큰을 제외한 마지막 토큰중 엘리먼트가 userTask인 경우를 찾는다
        val tokenToWithDraw = currentToken.instance.tokens!!.filter {
            it != currentToken && it.element.elementType == WfElementConstants.ElementType.USER_TASK.value
        }.maxWith(Comparator { o1, o2 ->
            when {
                o1.tokenStartDt!! > o2.tokenStartDt -> 1
                o1.tokenStartDt > o2.tokenStartDt -> 1
                else -> -1
            }
        }) ?: throw AliceException(AliceErrorConstants.ERR_00005, "Not found reject element in tokens.")

        this.createToken(tokenDto.copy(), tokenToWithDraw)
    }

    /**
     * action 처리 후 진행할 신규 토큰을 생성한다.
     */
    private fun createToken(newTokenDto: WfTokenDto, baseToken: WfTokenEntity) {
        newTokenDto.tokenId = ""
        newTokenDto.tokenStatus = WfTokenConstants.Status.RUNNING.code
        newTokenDto.documentId = baseToken.instance.document.documentId
        newTokenDto.instanceId = baseToken.instance.instanceId
        newTokenDto.elementId = baseToken.element.elementId
        newTokenDto.elementType = baseToken.element.elementType

        val tokenManager = WfTokenManagerFactory(wfTokenManagerService).createTokenManager(newTokenDto.elementType)
        tokenManager.createToken(newTokenDto)
    }

    /**
     * 현재 토큰의 데이터를 갱신한다
     */
    private fun updateToken(tokenDto: WfTokenDto): WfTokenEntity {
        val token = this.makeTokenToUpdate(tokenDto)

        val tokenDataEntities = this.getTokenDataEntities(token, tokenDto)
        if (tokenDataEntities.isNotEmpty()) {
            wfTokenManagerService.saveAllTokenData(tokenDataEntities)
        }

        return wfTokenManagerService.saveToken(token)
    }

    /**
     * Get tokenDataEntities.
     */
    private fun getTokenDataEntities(token: WfTokenEntity, tokenDto: WfTokenDto): MutableList<WfTokenDataEntity> {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                token = token,
                component = wfTokenManagerService.getComponent(tokenDataDto.componentId),
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }

        return tokenDataEntities
    }

    /**
     * Make token entity.
     */
    private fun makeTokenToUpdate(tokenDto: WfTokenDto): WfTokenEntity {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)

        // null 이면 담당자는 업데이트를 생략한다.
        token.assigneeId = tokenDto.assigneeId ?: token.assigneeId
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        when (tokenDto.action) {
            WfElementConstants.Action.CANCEL.value -> {
                token.tokenStatus = WfTokenConstants.Status.CANCEL.code
            }
            WfElementConstants.Action.TERMINATE.value -> {
                token.tokenStatus = WfTokenConstants.Status.TERMINATE.code
            }
            WfElementConstants.Action.WITHDRAW.value -> {
                token.tokenStatus = WfTokenConstants.Status.WITHDRAW.code
            }
            WfElementConstants.Action.REJECT.value -> {
                token.tokenStatus = WfTokenConstants.Status.REJECT.code
            }
            WfElementConstants.Action.SAVE.value -> {
                token.tokenEndDt = null
            }
        }

        return token
    }
}
