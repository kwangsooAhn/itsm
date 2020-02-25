package co.brainz.workflow.token.service

import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.service.WFInstanceService
import co.brainz.workflow.token.constants.TokenConstants
import co.brainz.workflow.token.dto.TokenDto
import co.brainz.workflow.token.dto.TokenSaveDto
import co.brainz.workflow.token.entity.TokenDataEntity
import co.brainz.workflow.token.entity.TokenMstEntity
import co.brainz.workflow.token.repository.TokenDataRepository
import co.brainz.workflow.token.repository.TokenMstRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFTokenService(private val tokenMstRepository: TokenMstRepository,
                     private val tokenDataRepository: TokenDataRepository,
                     private val wfInstanceService: WFInstanceService) {

    /**
     * Token Save.
     *
     * @param tokenSaveDto
     */
    fun postToken(tokenSaveDto: TokenSaveDto) {
        val instanceDto = InstanceDto(instanceId = "", processId = tokenSaveDto.processDto.id)
        val instance = wfInstanceService.createInstance(instanceDto)
        val token = createToken(instance.instanceId, tokenSaveDto.tokenDto)
        createTokenData(tokenSaveDto, instance.instanceId, token.tokenId)
        if (tokenSaveDto.tokenDto.isComplete) {
            tokenComplete()
        }
    }

    /**
     * Token Data Insert.
     *
     * @param tokenSaveDto
     * @param instanceId
     * @param tokenId
     */
    fun createTokenData(tokenSaveDto: TokenSaveDto, instanceId: String, tokenId: String) {
        val tokenDataEntities: MutableList<TokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenSaveDto.tokenDto.data!!) {
            val tokenDataEntity = TokenDataEntity(
                    instanceId = instanceId,
                    tokenId = tokenId,
                    componentId = tokenDataDto.componentId,
                    value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            tokenDataRepository.saveAll(tokenDataEntities)
        }
    }

    /**
     * Token Update.
     *
     * @param tokenSaveDto
     */
    fun putToken(tokenSaveDto: TokenSaveDto) {
        updateToken(tokenSaveDto.tokenDto)
        deleteTokenData(tokenSaveDto.instanceDto.id, tokenSaveDto.tokenDto.id)
        createTokenData(tokenSaveDto, tokenSaveDto.instanceDto.id, tokenSaveDto.tokenDto.id)
        if (tokenSaveDto.tokenDto.isComplete) {
            tokenComplete()
        }
    }

    /**
     * Token Create.
     *
     * @param tokenDto
     * @return TokenMstEntity
     */
    fun createToken(instanceId: String, tokenDto: TokenDto): TokenMstEntity {
        val tokenMstEntity = TokenMstEntity(
                tokenId = "",
                instanceId = instanceId,
                elementId = tokenDto.elementId,
                tokenStatus = TokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return tokenMstRepository.save(tokenMstEntity)
    }

    fun updateToken(tokenDto: TokenDto) {
        val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenDto.id)
        if (tokenMstEntity.isPresent) {
            tokenMstEntity.get().assigneeId = tokenDto.assigneeId
            tokenMstEntity.get().assigneeType = tokenDto.assigneeType
            tokenMstRepository.save(tokenMstEntity.get())
        }
    }

    /**
     * Token Data Delete.
     *
     * @param instanceId
     * @param tokenId
     */
    fun deleteTokenData(instanceId: String, tokenId: String) {
        tokenDataRepository.deleteTokenDataEntityByInstanceIdAndTokenId(instanceId, tokenId)
    }

    /**
     * Token Complete.
     *
     * @param tokenDto
     */
    fun completeToken(tokenDto: TokenDto) {
        val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenDto.id)
        if (tokenMstEntity.isPresent) {
            tokenMstEntity.get().tokenStatus = TokenConstants.Status.FINISH.code
            tokenMstEntity.get().tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            tokenMstRepository.save(tokenMstEntity.get())
        }
    }

    /**
     * Token Complete.
     */
    fun tokenComplete() {
        //TODO: Token Complete.
    }
}
