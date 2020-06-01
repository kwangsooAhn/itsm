package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto

class WfSignalSendTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    lateinit var assigneeId: String

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.createToken(wfTokenDto)
        //super.wfTokenEntity.tokenData = wfTokenDataRepository.saveAll(setTokenData(wfTokenDto))
        //this.assigneeId = wfTokenDto.assigneeId.toString()
        //setCandidate(super.wfTokenEntity)

        return wfTokenDto
    }

}
