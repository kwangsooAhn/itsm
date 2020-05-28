package co.brainz.workflow.engine.manager.impl

import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.entity.WfTokenDataEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import java.time.LocalDateTime
import java.time.ZoneId

class WfUserTaskTokenManager(
    private val wfElementService: WfElementService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository
) : WfTokenManager {

    override fun createToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val token = WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(restTemplateTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(restTemplateTokenDto.elementId),
            assigneeId = getAssignee(restTemplateTokenDto)
        )
        restTemplateTokenDto.tokenId = wfTokenRepository.save(token).tokenId
        restTemplateTokenDto.elementId = token.element.elementId
        restTemplateTokenDto.elementType = token.element.elementType

        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in restTemplateTokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                tokenId = restTemplateTokenDto.tokenId,
                componentId = tokenDataDto.componentId,
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (!tokenDataEntities.isNullOrEmpty()) {
            token.tokenData = tokenDataEntities
            wfTokenDataRepository.saveAll(tokenDataEntities)
        }

        return restTemplateTokenDto
    }

    override fun createNextToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val element = wfElementService.getNextElement(restTemplateTokenDto)
        restTemplateTokenDto.elementId = element.elementId
        restTemplateTokenDto.elementType = element.elementType
        val tokenManager = WfTokenManagerFactory(
            wfElementService,
            wfInstanceRepository,
            wfElementRepository,
            wfTokenRepository,
            wfTokenDataRepository
        ).getTokenManager(restTemplateTokenDto.elementType)

        return tokenManager.createToken(restTemplateTokenDto)
    }

    override fun completeToken(restTemplateTokenDto: RestTemplateTokenDto): RestTemplateTokenDto {
        val token = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
        token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        token.tokenStatus = RestTemplateConstants.TokenStatus.FINISH.value
        wfTokenRepository.save(token)

        //TODO: token 데이터를 갱신한다.
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in restTemplateTokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                tokenId = restTemplateTokenDto.tokenId,
                componentId = tokenDataDto.componentId,
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (!tokenDataEntities.isNullOrEmpty()) {
            token.tokenData = tokenDataEntities
            wfTokenDataRepository.saveAll(tokenDataEntities)
        }

        return restTemplateTokenDto
    }

    fun getAssignee(restTemplateTokenDto: RestTemplateTokenDto): String {
        //저장이면.. 담당자를 로그인한 사람으로 넣는다. candidate는 넣고
        return restTemplateTokenDto.assigneeId.toString()
    }

}
