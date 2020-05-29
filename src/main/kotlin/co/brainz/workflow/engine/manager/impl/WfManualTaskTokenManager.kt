package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto

class WfManualTaskTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfTokenRepository = constructorManager.getTokenRepository()

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.createToken(wfTokenDto)
        super.wfTokenEntity.assigneeId = wfTokenDto.assigneeId
        wfTokenRepository.save(super.wfTokenEntity)
        return wfTokenDto
    }

}
