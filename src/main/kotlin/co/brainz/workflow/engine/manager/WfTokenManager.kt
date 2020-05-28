package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import java.time.ZoneId

abstract class WfTokenManager {

    lateinit var wfTokenEntity: WfTokenEntity

    abstract val wfElementService: WfElementService
    abstract val wfInstanceService: WfInstanceService
    abstract val wfInstanceRepository: WfInstanceRepository
    abstract val wfElementRepository: WfElementRepository
    abstract val wfTokenRepository: WfTokenRepository
    abstract val wfTokenDataRepository: WfTokenDataRepository
    abstract val wfCandidateRepository: WfCandidateRepository

    open fun createToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(restTemplateTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(restTemplateTokenDto.elementId)
        )
        this.wfTokenEntity = wfTokenRepository.save(token)
        restTemplateTokenDto.tokenId = this.wfTokenEntity.tokenId
        restTemplateTokenDto.elementId = this.wfTokenEntity.element.elementId
        restTemplateTokenDto.elementType = this.wfTokenEntity.element.elementType
        return restTemplateTokenDto
    }

    open fun createNextToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val element = wfElementService.getNextElement(restTemplateTokenDto)
        restTemplateTokenDto.elementId = element.elementId
        restTemplateTokenDto.elementType = element.elementType
        val tokenManager = WfTokenManagerFactory(
            wfElementService,
            wfInstanceService,
            wfInstanceRepository,
            wfElementRepository,
            wfTokenRepository,
            wfTokenDataRepository,
            wfCandidateRepository
        ).getTokenManager(restTemplateTokenDto.elementType)
        return tokenManager.createToken(restTemplateTokenDto)
    }

    open fun completeToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val token = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        this.wfTokenEntity = wfTokenRepository.save(token)
        return restTemplateTokenDto
    }
}
