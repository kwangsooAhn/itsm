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
import co.brainz.workflow.document.repository.DocumentRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFTokenService(private val documentRepository: DocumentRepository,
                     private val tokenMstRepository: TokenMstRepository,
                     private val tokenDataRepository: TokenDataRepository,
                     private val wfInstanceService: WFInstanceService) {

    /**
     * Token Save.
     *
     * @param tokenSaveDto
     */
    fun postToken(tokenSaveDto: TokenSaveDto) {
        val documentDto = documentRepository.findDocumentEntityByDocumentId(tokenSaveDto.documentDto.documentId)
        val processId = documentDto.processes.processId;
        val instanceDto = InstanceDto(instanceId = "", processId = processId)
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
        val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenSaveDto.tokenDto.tokenId)
        if (tokenMstEntity.isPresent) {
            val instanceId = tokenMstEntity.get().instanceId
            updateToken(tokenSaveDto.tokenDto)
            deleteTokenData(instanceId, tokenSaveDto.tokenDto.tokenId)
            createTokenData(tokenSaveDto, instanceId, tokenSaveDto.tokenDto.tokenId)
            if (tokenSaveDto.tokenDto.isComplete) {
                tokenComplete()
            }
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
        val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenDto.tokenId)
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
        val tokenMstEntity = tokenMstRepository.findTokenMstEntityByTokenId(tokenDto.tokenId)
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
