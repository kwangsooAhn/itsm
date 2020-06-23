package co.brainz.workflow.engine.manager.service

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
        }
    }

    /**
     * Save.
     */
    private fun actionSave(tokenDto: WfTokenDto) {
        this.tokenComplete(tokenDto)
    }

    /**
     * Cancel.
     */
    private fun actionCancel(tokenDto: WfTokenDto) {
        val token = this.tokenComplete(tokenDto)
        wfTokenManagerService.completeInstance(token.instance.instanceId)
    }

    /**
     * Terminate.
     */
    private fun actionTerminate(tokenDto: WfTokenDto) {
        val token = this.tokenComplete(tokenDto)

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
        val tokenManager = WfTokenManagerFactory(wfTokenManagerService).getTokenManager(commonEndTokenDto.elementType)
        WfEngine(wfTokenManagerService).progressWorkflow(tokenManager.createToken(commonEndTokenDto))
    }

    /**
     * Token Complete.
     */
    private fun tokenComplete(tokenDto: WfTokenDto): WfTokenEntity {
        val token = this.tokenDtoToEntity(tokenDto)

        // save, cancel, terminate 모두 현재 토큰의 데이터를 갱신한다.
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
    private fun tokenDtoToEntity(tokenDto: WfTokenDto): WfTokenEntity {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.assigneeId = tokenDto.assigneeId
        when (tokenDto.action) {
            WfTokenConstants.Status.CANCEL.code -> {
                token.tokenStatus = WfTokenConstants.Status.CANCEL.code
                token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
            WfTokenConstants.Status.TERMINATE.code -> {
                token.tokenStatus = WfTokenConstants.Status.TERMINATE.code
                token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
        }

        return token
    }
}
