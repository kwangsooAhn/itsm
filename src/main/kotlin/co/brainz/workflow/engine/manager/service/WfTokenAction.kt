package co.brainz.workflow.engine.manager.service

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenDataEntity

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
        if (tokenDto.tokenStatus != null) {
            token.tokenStatus = tokenDto.tokenStatus.toString()
        }
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
        tokenDto.tokenStatus = WfTokenConstants.Status.CANCEL.code
        this.save(tokenDto)
        wfTokenManagerService.completeInstance(tokenDto.instanceId)
    }

    /**
     * Terminate.
     */
    private fun terminate(tokenDto: WfTokenDto) {
        tokenDto.tokenStatus = WfTokenConstants.Status.TERMINATE.code
        this.save(tokenDto)
        wfTokenManagerService.completeInstance(tokenDto.instanceId)
    }
}
