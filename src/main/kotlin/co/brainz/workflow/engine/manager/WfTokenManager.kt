package co.brainz.workflow.engine.manager

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
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

    open fun createToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
        this.wfTokenEntity = wfTokenRepository.save(token)
        wfTokenDto.tokenId = this.wfTokenEntity.tokenId
        wfTokenDto.elementId = this.wfTokenEntity.element.elementId
        wfTokenDto.elementType = this.wfTokenEntity.element.elementType
        return wfTokenDto
    }

    open fun createNextToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val element = wfElementService.getNextElement(wfTokenDto)
        wfTokenDto.elementId = element.elementId
        wfTokenDto.elementType = element.elementType
        val tokenManager = WfTokenManagerFactory(
            wfElementService,
            wfInstanceService,
            wfInstanceRepository,
            wfElementRepository,
            wfTokenRepository,
            wfTokenDataRepository,
            wfCandidateRepository
        ).getTokenManager(wfTokenDto.elementType)
        return tokenManager.createToken(wfTokenDto)
    }

    open fun completeToken(wfTokenDto: WfTokenDto): WfTokenDto {
        val token = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        this.wfTokenEntity = wfTokenRepository.save(token)
        return wfTokenDto
    }
}
