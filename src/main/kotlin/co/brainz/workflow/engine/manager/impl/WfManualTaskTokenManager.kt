package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto

class WfManualTaskTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfTokenManagerService = constructorManager.getTokenManagerService()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenManagerService.makeTokenEntity(wfTokenDto)
        val saveToken = wfTokenRepository.save(token)
        wfTokenDto.tokenId = saveToken.tokenId
        wfTokenDto.elementId = saveToken.element.elementId
        wfTokenDto.elementType = saveToken.element.elementType

        token.assigneeId = wfTokenDto.assigneeId
        wfTokenRepository.save(token)
        return wfTokenDto
    }

}
