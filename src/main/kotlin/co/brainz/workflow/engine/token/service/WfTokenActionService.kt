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
     * Init Registration.
     *
     * @param wfTokenDto
     * @param instance
     */
    fun initRegistration(wfTokenDto: WfTokenDto, instance: WfInstanceEntity?) {
        wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
        val token = instance?.let { createToken(it, wfTokenDto) }
        if (token != null) {
            wfTokenDto.tokenId = token.tokenId
            createTokenData(wfTokenDto, token.tokenId)
        }
    }

    /**
     * Init Save.
     *
     * @param wfTokenDto
     * @param instance
     */
    fun initSave(wfTokenDto: WfTokenDto, instance: WfInstanceEntity?) {
        val token = instance?.let { createToken(it, wfTokenDto) }
        if (token != null) {
            wfTokenDto.tokenId = token.tokenId
            createTokenData(wfTokenDto, token.tokenId)
        }
    }

    /**
     * Registration.
     *
     * @param wfTokenEntity
     * @param
     *
     */
    fun registration(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {
        wfTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
        wfTokenDto.assigneeId = wfTokenEntity.assigneeId
        wfTokenDto.assigneeType = wfTokenEntity.assigneeType
        updateToken(wfTokenEntity, wfTokenDto)
        deleteTokenData(wfTokenDto.tokenId)
        createTokenData(wfTokenDto, wfTokenDto.tokenId)
    }

    /**
     * Save.
     *
     * @param wfTokenEntity
     * @return Boolean
     */
    fun save(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto): Boolean {
        wfTokenDto.tokenStatus = WfTokenConstants.Status.RUNNING.code
        updateToken(wfTokenEntity, wfTokenDto)
        deleteTokenData(wfTokenDto.tokenId)
        createTokenData(wfTokenDto, wfTokenDto.tokenId)
        return true
    }

    /**
     * Reject.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     * @return Boolean
     */
    fun reject(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto, values: HashMap<String, Any>): Boolean {
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
     * Withdraw.
     *
     * @param wfTokenEntity
     * @param wfTokenDto
     */
    fun withdraw(wfTokenEntity: WfTokenEntity, wfTokenDto: WfTokenDto) {

    }

    /**
     * Token Create.
     *
     * @param wfTokenDto
     * @return TokenEntity
     */
    private fun createToken(wfInstance: WfInstanceEntity, wfTokenDto: WfTokenDto): WfTokenEntity {
        val tokenEntity = WfTokenEntity(
                tokenId = "",
                elementId = wfTokenDto.elementId,
                tokenStatus = wfTokenDto.tokenStatus?:WfTokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                assigneeId = wfTokenDto.assigneeId,
                assigneeType = wfTokenDto.assigneeType,
                instance = wfInstance
        )
        return wfTokenRepository.save(tokenEntity)
    }

    /**
     * Token Data Insert.
     *
     * @param wfTokenDto
     * @param tokenId
     */
    private fun createTokenData(wfTokenDto: WfTokenDto, tokenId: String) {
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
