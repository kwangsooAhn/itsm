package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import java.time.LocalDateTime
import java.time.ZoneId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfTokenActionService(
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfElementRepository: WfElementRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Token Process (End Token).
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     */
    fun setProcess(wfTokenEntity: WfTokenEntity, restTemplateTokenDto: RestTemplateTokenDto) {
        wfTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        restTemplateTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
        updateToken(wfTokenEntity, restTemplateTokenDto)
        // 현재 Element 의 데이터를 갱신 (다음 Element 로 넘어가는 데이터와 동일한 값으로 업데이트)
        deleteTokenData(restTemplateTokenDto.tokenId)
        createTokenData(restTemplateTokenDto, restTemplateTokenDto.tokenId)
    }

    /**
     * Token Save.
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     * @return Boolean
     */
    fun save(wfTokenEntity: WfTokenEntity, restTemplateTokenDto: RestTemplateTokenDto): Boolean {
        restTemplateTokenDto.tokenStatus = WfTokenConstants.Status.RUNNING.code
        restTemplateTokenDto.assigneeId = wfTokenEntity.assigneeId
        updateToken(wfTokenEntity, restTemplateTokenDto)
        deleteTokenData(restTemplateTokenDto.tokenId)
        createTokenData(restTemplateTokenDto, restTemplateTokenDto.tokenId)
        return true
    }

    /**
     * Token Reject.
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     * @param values
     * @return Boolean
     */
    fun setReject(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto,
        values: HashMap<String, Any>
    ): WfTokenEntity {
        // TODO: 확인 필요
        wfTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
        wfTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        wfTokenRepository.save(wfTokenEntity)

        // Create Reject Token
        val rejectToken = WfTokenEntity(
            tokenId = "",
            tokenStatus = WfTokenConstants.Status.RUNNING.code,
            assigneeId = values[WfElementConstants.AttributeId.ASSIGNEE.value] as String,
            element = wfElementRepository.findWfElementEntityByElementId(values[WfElementConstants.AttributeId.REJECT_ID.value] as String),
            instance = wfTokenEntity.instance,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return wfTokenRepository.save(rejectToken)
    }

    /**
     * Token Withdraw.
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     */
    fun setWithdraw(wfTokenEntity: WfTokenEntity, restTemplateTokenDto: RestTemplateTokenDto) {
    }

    /**
     * Token Create.
     *
     * @param restTemplateTokenDto
     * @return TokenEntity
     */
    fun createToken(wfInstance: WfInstanceEntity, restTemplateTokenDto: RestTemplateTokenDto): WfTokenEntity {
        val tokenEntity = WfTokenEntity(
            tokenId = "",
            element = wfElementRepository.findWfElementEntityByElementId(restTemplateTokenDto.elementId),
            tokenStatus = restTemplateTokenDto.tokenStatus ?: WfTokenConstants.Status.RUNNING.code,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstance
        )
        if (restTemplateTokenDto.tokenStatus == WfTokenConstants.Status.FINISH.code) {
            tokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        }
        return wfTokenRepository.save(tokenEntity)
    }

    /**
     * Token Data Insert.
     *
     * @param restTemplateTokenDto
     * @param tokenId
     */
    fun createTokenData(restTemplateTokenDto: RestTemplateTokenDto, tokenId: String) {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in restTemplateTokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                tokenId = tokenId,
                componentId = tokenDataDto.componentId,
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            wfTokenDataRepository.saveAll(tokenDataEntities)
        }
    }

    /**
     * Token Update.
     *
     * @param restTemplateTokenDto
     */
    fun updateToken(wfTokenEntity: WfTokenEntity, restTemplateTokenDto: RestTemplateTokenDto) {
        wfTokenEntity.assigneeId = restTemplateTokenDto.assigneeId
        wfTokenEntity.tokenStatus = restTemplateTokenDto.tokenStatus ?: WfTokenConstants.Status.RUNNING.code
        wfTokenRepository.save(wfTokenEntity)
    }

    /**
     * Token Data Delete.
     *
     * @param tokenId
     */
    fun deleteTokenData(tokenId: String) {
        wfTokenDataRepository.deleteTokenDataEntityByTokenId(tokenId)
    }
}
