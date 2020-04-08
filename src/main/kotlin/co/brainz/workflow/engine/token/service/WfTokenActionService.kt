package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfTokenActionService(private val wfTokenRepository: WfTokenRepository,
                           private val wfTokenDataRepository: WfTokenDataRepository) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Token Process (End Token).
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun setProcess(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        wfTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
        wfTokenDto.assigneeId = wfTokenEntity.assigneeId
        wfTokenDto.assigneeType = wfTokenEntity.assigneeType
        updateToken(wfTokenEntity, wfTokenDto)
        //아래를 막으면 이전데이터를 현재 element에 반영하지 않는다.
        deleteTokenData(wfTokenDto.tokenId)
        createTokenData(wfTokenDto, wfTokenDto.tokenId)
    }

    /**
     * Token Save.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     * @return Boolean
     */
    fun save(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto): Boolean {
        wfTokenDto.tokenStatus = WfTokenConstants.Status.RUNNING.code
        wfTokenDto.assigneeId = wfTokenEntity.assigneeId
        wfTokenDto.assigneeType = wfTokenEntity.assigneeType
        updateToken(wfTokenEntity, wfTokenDto)
        deleteTokenData(wfTokenDto.tokenId)
        createTokenData(wfTokenDto, wfTokenDto.tokenId)
        return true
    }

    /**
     * Token Reject.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     * @param values
     * @return Boolean
     */
    fun setReject(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto, values: HashMap<String, Any>): Boolean {
        //TODO: 확인 필요
        wfTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
        wfTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        wfTokenRepository.save(wfTokenEntity)

        //Create Reject Token
        val rejectToken = WfTokenEntity(
                tokenId = "",
                tokenStatus = WfTokenConstants.Status.RUNNING.code,
                assigneeId = values[WfElementConstants.AttributeId.ASSIGNEE.value] as String,
                assigneeType = values[WfElementConstants.AttributeId.ASSIGNEE_TYPE.value] as String,
                elementId = values[WfElementConstants.AttributeId.REJECT_ID.value] as String,
                instance = wfTokenEntity.instance,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        wfTokenRepository.save(rejectToken)

        return true
    }

    /**
     * Token Withdraw.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun setWithdraw(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {

    }

    /**
     * Token Create.
     *
     * @param wfTokenDto
     * @return TokenEntity
     */
    fun createToken(wfInstance: WfInstanceEntity, wfTokenDto: WfTokenDto): WfTokenEntity {
        val tokenEntity = WfTokenEntity(
                tokenId = "",
                elementId = wfTokenDto.elementId,
                tokenStatus = wfTokenDto.tokenStatus?:WfTokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                assigneeId = wfTokenDto.assigneeId,
                assigneeType = wfTokenDto.assigneeType,
                instance = wfInstance
        )
        if (wfTokenDto.tokenStatus == WfTokenConstants.Status.FINISH.code) {
            tokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
        }
        return wfTokenRepository.save(tokenEntity)
    }

    /**
     * Token Data Insert.
     *
     * @param wfTokenDto
     * @param tokenId
     */
    fun createTokenData(wfTokenDto: WfTokenDto, tokenId: String) {
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in wfTokenDto.data!!) {
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
     * @param wfTokenDto
     */
    fun updateToken(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        wfTokenEntity.assigneeId = wfTokenDto.assigneeId
        wfTokenEntity.assigneeType = wfTokenDto.assigneeType
        wfTokenEntity.tokenStatus = wfTokenDto.tokenStatus?:WfTokenConstants.Status.RUNNING.code
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
