package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
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

    override fun createToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        restTemplateTokenDto.isComplete = false
        return super.createToken(restTemplateTokenDto)
    }

    override fun createNextToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        restTemplateTokenDto.isComplete = true //반복문을 종료한다.
        return restTemplateTokenDto
    }

    override fun completeToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        super.completeToken(restTemplateTokenDto)
        wfInstanceService.completeInstance(restTemplateTokenDto.instanceId)

        return restTemplateTokenDto
    }

}
