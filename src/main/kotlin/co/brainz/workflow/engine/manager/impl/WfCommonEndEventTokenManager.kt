package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager

class WfCommonEndEventTokenManager(
    constructorManager: ConstructorManager
) : WfTokenManager(constructorManager) {

    private val wfInstanceService = constructorManager.getInstanceService()

    override fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        wfTokenDto.isAutoComplete = false //반복문을 종료한다.
        return wfTokenDto
    }

    override fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        super.completeToken(wfTokenDto)
        wfInstanceService.completeInstance(wfTokenDto.instanceId)

        return wfTokenDto
    }

}
