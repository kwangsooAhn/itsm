package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository

class WfCommonEndEventTokenManager(
    override val wfElementService: WfElementService,
    override val wfInstanceService: WfInstanceService,
    override val wfInstanceRepository: WfInstanceRepository,
    override val wfElementRepository: WfElementRepository,
    override val wfTokenRepository: WfTokenRepository,
    override val wfTokenDataRepository: WfTokenDataRepository,
    override val wfCandidateRepository: WfCandidateRepository
) : WfTokenManager() {

    override fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        wfTokenDto.isAutoComplete = true
        return super.createToken(wfTokenDto)
    }

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
