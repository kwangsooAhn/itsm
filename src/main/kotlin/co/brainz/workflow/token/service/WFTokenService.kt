package co.brainz.workflow.token.service

import co.brainz.workflow.document.repository.DocumentRepository
import co.brainz.workflow.element.constants.ElementConstants
import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.element.service.WFElementService
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.service.WFInstanceService
import co.brainz.workflow.token.constants.TokenConstants
import co.brainz.workflow.token.dto.TokenDto
import co.brainz.workflow.token.dto.TokenSaveDto
import co.brainz.workflow.token.entity.TokenDataEntity
import co.brainz.workflow.token.entity.TokenEntity
import co.brainz.workflow.token.repository.TokenDataRepository
import co.brainz.workflow.token.repository.TokenRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
@Transactional
class WFTokenService(private val documentRepository: DocumentRepository,
                     private val tokenRepository: TokenRepository,
                     private val tokenDataRepository: TokenDataRepository,
                     private val wfInstanceService: WFInstanceService,
                     private val wfElementService: WFElementService) {

    /**
     * Token Save.
     *
     * @param tokenSaveDto
     */
    fun postToken(tokenSaveDto: TokenSaveDto) {
        val documentDto = documentRepository.findDocumentEntityByDocumentId(tokenSaveDto.documentDto.documentId)
        val processId = documentDto.processes.processId
        val instanceDto = InstanceDto(instanceId = "", processId = processId)
        val instance = wfInstanceService.createInstance(instanceDto)
        //postToken에서는 문서를 새롭게 만드는 메소드이기 때문에 elementId를 강제로 얻어와야 한다.
        tokenSaveDto.tokenDto.elementId =  wfElementService.getElementId(processId, ElementConstants.ElementStatusType.START.value).elementId
        val token = createToken(instance.instanceId, tokenSaveDto.tokenDto)
        createTokenData(tokenSaveDto, instance.instanceId, token.tokenId)
        tokenSaveDto.tokenDto.tokenId = token.tokenId
        if (tokenSaveDto.tokenDto.isComplete) {
            completeToken(tokenSaveDto)
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
        val tokenEntity = tokenRepository.findTokenEntityByTokenId(tokenSaveDto.tokenDto.tokenId)
        if (tokenEntity.isPresent) {
            val instanceId = tokenEntity.get().instanceId
            updateToken(tokenSaveDto.tokenDto)
            deleteTokenData(instanceId, tokenSaveDto.tokenDto.tokenId)
            createTokenData(tokenSaveDto, instanceId, tokenSaveDto.tokenDto.tokenId)
            if (tokenSaveDto.tokenDto.isComplete) {
                completeToken(tokenSaveDto)
            }
        }
    }

    /**
     * Token Create.
     *
     * @param tokenDto
     * @return TokenEntity
     */
    fun createToken(instanceId: String, tokenDto: TokenDto): TokenEntity {
        val tokenEntity = TokenEntity(
                tokenId = "",
                instanceId = instanceId,
                elementId = tokenDto.elementId,
                tokenStatus = TokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return tokenRepository.save(tokenEntity)
    }

    /**
     * Token Update.
     *
     * @param tokenDto
     */
    fun updateToken(tokenDto: TokenDto) {
        val tokenEntity = tokenRepository.findTokenEntityByTokenId(tokenDto.tokenId)
        if (tokenEntity.isPresent) {
            tokenEntity.get().assigneeId = tokenDto.assigneeId
            tokenEntity.get().assigneeType = tokenDto.assigneeType
            tokenRepository.save(tokenEntity.get())
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
     * @param tokenSaveDto
     */
    fun completeToken(tokenSaveDto: TokenSaveDto) {
        // Optional -> 안쓰면 좋을 것 같은데...
        val completedTokenOptional = tokenRepository.findTokenEntityByTokenId(tokenSaveDto.tokenDto.tokenId)

        if (completedTokenOptional.isPresent) {
            val completedToken = completedTokenOptional.get()

            // 토큰 완료 처리
            completedToken.tokenStatus = TokenConstants.Status.FINISH.code
            completedToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            tokenRepository.save(completedToken)

            //  다음 Element 가져오기
            val nextElement: ElementEntity? = wfElementService.getNextElement(completedToken.elementId, tokenSaveDto)
            nextElement?.let { it ->
                val assigneeValueInNextElement: String? = it.getElementDataValue(ElementConstants.AttributeId.ASSIGNEE.value)
                val assigneeTypeValueInNextElement: String? = it.getElementDataValue(ElementConstants.AttributeId.ASSIGNEE_TYPE.value)

                val nextToken = TokenDto(tokenId = "",
                          isComplete = false,
                          elementId = it.elementId,
                          assigneeId = getAssigneeForToken(assigneeValueInNextElement),
                          assigneeType = assigneeTypeValueInNextElement,
                          data = null
                )

                when (it.elementType) {
                    ElementConstants.ElementType.USER_TASK.value -> {
                        val createdToken = createToken(completedToken.instanceId, nextToken)
                        createTokenData(tokenSaveDto, completedToken.instanceId, createdToken.tokenId)
                    }
                    ElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                        val createdToken = createToken(completedToken.instanceId, nextToken)
                        createTokenData(tokenSaveDto, completedToken.instanceId, createdToken.tokenId)
                        completeToken(TokenSaveDto(tokenSaveDto.documentDto,  nextToken))
                    }
                    ElementConstants.ElementType.COMMON_END_EVENT.value -> {
                        wfInstanceService.completeInstance(completedToken.instanceId)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun getAssigneeForToken(assigneeData: String?): String {
        // 데이터가 만약 mappingExpresion('${xxxx}'와 같은) 이라면 문서에서 해당 코드로 매핑된 데이터를 찾아서 사용.
        // 데이터가 단순히 문자열이라면 그대로 사용.
        var assigneeForToken = ""

        val mappingExpr = TokenConstants.mappingExpression.toRegex()
        assigneeData?.let {
            assigneeForToken = if (mappingExpr.matches(assigneeData)) it.substring (2, assigneeData.length-1) else assigneeData
        }
        return assigneeForToken
    }
}
