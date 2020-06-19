package co.brainz.workflow.engine.manager.service

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity
import java.time.LocalDateTime
import java.time.ZoneId

class WfTokenAction(
    private val wfTokenManagerService: WfTokenManagerService
) {

    fun action(tokenDto: WfTokenDto) {
        when (tokenDto.action) {
            WfElementConstants.Action.SAVE.value -> this.save(tokenDto)
            WfElementConstants.Action.CANCEL.value -> this.cancel(tokenDto)
            WfElementConstants.Action.TERMINATE.value -> this.terminate(tokenDto)
        }
    }

    /**
     * Save.
     */
    private fun save(tokenDto: WfTokenDto) {
        // Save Token & Token Data
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.assigneeId = tokenDto.assigneeId
        token.tokenEndDt
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
     * Cancel.
     */
    private fun cancel(tokenDto: WfTokenDto) {
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.tokenStatus = WfTokenConstants.Status.CANCEL.code
        token.assigneeId = tokenDto.assigneeId
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        wfTokenManagerService.saveToken(token)
        wfTokenManagerService.completeInstance(tokenDto.instanceId)
    }

    /**
     * Terminate.
     */
    private fun terminate(tokenDto: WfTokenDto) {
        //token terminate
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.tokenStatus = WfTokenConstants.Status.TERMINATE.code
        token.assigneeId = tokenDto.assigneeId
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        wfTokenManagerService.saveToken(token)

        //end token
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
}
