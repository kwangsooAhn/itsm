package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import java.time.ZoneId

class WfCommonEndEventTokenManager(
    private val wfInstanceService: WfInstanceService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfTokenRepository: WfTokenRepository
) : WfTokenManager {

    override fun createToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(restTemplateTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(restTemplateTokenDto.elementId)
        )
        restTemplateTokenDto.tokenId = wfTokenRepository.save(token).tokenId
        restTemplateTokenDto.elementId = token.element.elementId
        restTemplateTokenDto.elementType = token.element.elementType
        restTemplateTokenDto.isComplete = false
        return restTemplateTokenDto
    }

    override fun createNextToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        restTemplateTokenDto.isComplete = true //반복문을 종료한다.
        return restTemplateTokenDto

    }

    override fun completeToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val token = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        wfTokenRepository.save(token)

        wfInstanceService.completeInstance(restTemplateTokenDto.instanceId)

        return restTemplateTokenDto
    }

}
