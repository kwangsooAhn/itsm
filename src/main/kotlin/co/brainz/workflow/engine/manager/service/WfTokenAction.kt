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

class WfTokenAction(
    private val wfTokenManagerService: WfTokenManagerService
) {

    fun progressApplicationAction(tokenDto: WfTokenDto) {
        when (tokenDto.action) {
            WfElementConstants.Action.SAVE.value -> this.actionSave(tokenDto)
            WfElementConstants.Action.CANCEL.value -> this.actionCancel(tokenDto)
            WfElementConstants.Action.TERMINATE.value -> this.actionTerminate(tokenDto)
            WfElementConstants.Action.WITHDRAW.value -> this.actionWithdraw(tokenDto)
            WfElementConstants.Action.REJECT.value -> this.actionReject(tokenDto)
            WfElementConstants.Action.REVIEW.value -> this.actionReview(tokenDto)
        }
    }

    /**
     * Review
     */
    fun actionReview(tokenDto: WfTokenDto): Boolean {
        return wfTokenManagerService.updateReview(tokenDto)
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
            elementType = element.elementType,
            action = WfElementConstants.Action.PROGRESS.value,
            assigneeId = token.assigneeId,
            data = tokenDto.data
        )
        val tokenManager =
            WfTokenManagerFactory(wfTokenManagerService).createTokenManager(commonEndTokenDto)
        WfEngine(wfTokenManagerService).progressWorkflow(tokenManager.createToken(commonEndTokenDto))
    }

    /**
     * Action - ??????
     *
     * ?????? ????????? ????????? ????????? ?????????????????? ????????? ?????? ??????????????? ????????? ?????? ????????? ????????????
     * ???????????? [tokenDto] ??? tokenId ??? ??????????????? ?????? ?????? ?????? ????????? ????????????
     */
    private fun actionReject(tokenDto: WfTokenDto) {
        // ?????? ????????? ???????????? ????????? reject ??? ????????????
        val currentToken = this.updateToken(tokenDto)

        // ?????? ???????????? ???????????? reject-id ??? ???????????? ????????? ??????????????? ????????????.
        val elementIdToReject = currentToken.element.getElementDataValue(WfElementConstants.AttributeId.REJECT_ID.value)
            ?: throw AliceException(AliceErrorConstants.ERR_00005, "Not found reject element data. check reject-id.")

        // ??????????????? ?????? ????????? ??? ????????? ??????????????? ????????? ?????? ????????? ????????????.(????????? ???????????? ??? ??????)
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
     * Action - ??????
     *
     * [tokenDto] ??? tokenId ??? ??????????????? ?????? ?????? ?????? ????????? ?????????
     */
    private fun actionWithdraw(tokenDto: WfTokenDto) {
        // ?????? ????????? ??????????????? ???????????? ????????? widthdraw ??? ??????????????????.
        // assignee ??? ??????????????? ???????????? ?????? null ??? ?????????.
        val updateTokenDto = tokenDto.copy()
        updateTokenDto.assigneeId = null
        val currentToken = this.updateToken(updateTokenDto)

        // ?????? ????????? ????????? ????????? ????????? ??????????????? userTask??? ????????? ?????????
        val tokenToWithDraw = currentToken.instance.tokens!!.filter {
            it != currentToken && it.element.elementType == WfElementConstants.ElementType.USER_TASK.value
        }.maxWith(Comparator { o1, o2 ->
            when {
                o1.tokenStartDt!! > o2.tokenStartDt -> 1
                o1.tokenStartDt == o2.tokenStartDt -> 0
                else -> -1
            }
        }) ?: throw AliceException(AliceErrorConstants.ERR_00005, "Not found reject element in tokens.")

        this.createToken(tokenDto.copy(), tokenToWithDraw)
    }

    /**
     * action ?????? ??? ????????? ?????? ????????? ????????????.
     */
    private fun createToken(newTokenDto: WfTokenDto, baseToken: WfTokenEntity) {
        newTokenDto.tokenId = ""
        newTokenDto.tokenStatus = WfTokenConstants.Status.RUNNING.code
        newTokenDto.documentId = baseToken.instance.document.documentId
        newTokenDto.instanceId = baseToken.instance.instanceId
        newTokenDto.elementId = baseToken.element.elementId
        newTokenDto.elementType = baseToken.element.elementType
        // ?????? ?????? ?????? ??? ?????? ???????????? ??????
        newTokenDto.assigneeId = baseToken.assigneeId
        val tokenManager = WfTokenManagerFactory(wfTokenManagerService).createTokenManager(newTokenDto)
        tokenManager.createToken(newTokenDto)
    }

    /**
     * ?????? ????????? ???????????? ????????????
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
        if (!tokenDto.data.isNullOrEmpty()) {
            for (tokenDataDto in tokenDto.data!!) {
                val tokenDataEntity = WfTokenDataEntity(
                    token = token,
                    component = wfTokenManagerService.getComponent(tokenDataDto.componentId),
                    value = tokenDataDto.value
                )
                tokenDataEntities.add(tokenDataEntity)
            }
        }

        return tokenDataEntities
    }

    /**
     * Make token entity.
     */
    private fun makeTokenToUpdate(tokenDto: WfTokenDto): WfTokenEntity {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)

        // null ?????? ???????????? ??????????????? ????????????.
        token.assigneeId = tokenDto.assigneeId ?: token.assigneeId
        token.tokenEndDt = LocalDateTime.now()
        when (tokenDto.action) {
            WfElementConstants.Action.CANCEL.value -> {
                token.tokenAction = WfTokenConstants.FinishAction.CANCEL.code
                token.tokenStatus = WfTokenConstants.Status.FINISH.code
            }
            WfElementConstants.Action.TERMINATE.value -> {
                token.tokenAction = WfTokenConstants.FinishAction.TERMINATE.code
                token.tokenStatus = WfTokenConstants.Status.FINISH.code
            }
            WfElementConstants.Action.WITHDRAW.value -> {
                token.tokenAction = WfTokenConstants.FinishAction.WITHDRAW.code
                token.tokenStatus = WfTokenConstants.Status.FINISH.code
            }
            WfElementConstants.Action.REJECT.value -> {
                token.tokenAction = WfTokenConstants.FinishAction.REJECT.code
                token.tokenStatus = WfTokenConstants.Status.FINISH.code
            }
            WfElementConstants.Action.SAVE.value -> {
                token.tokenEndDt = null
            }
        }

        return token
    }
}
