package co.brainz.workflow.token.service

import co.brainz.workflow.document.repository.DocumentRepository
import co.brainz.workflow.element.constants.ElementConstants
import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.element.service.WFElementService
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.entity.InstanceEntity
import co.brainz.workflow.instance.service.WFInstanceService
import co.brainz.workflow.token.constants.TokenConstants
import co.brainz.workflow.token.dto.ActionDto
import co.brainz.workflow.token.dto.TokenDataDto
import co.brainz.workflow.token.dto.TokenDto
import co.brainz.workflow.token.dto.TokenSaveDto
import co.brainz.workflow.token.dto.TokenViewDto
import co.brainz.workflow.token.entity.TokenDataEntity
import co.brainz.workflow.token.entity.TokenEntity
import co.brainz.workflow.token.repository.TokenDataRepository
import co.brainz.workflow.token.repository.TokenRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
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
     * Search Tokens.
     *
     * @param parameters
     * @return List<TokenDto>
     */
    fun getTokens(parameters: LinkedHashMap<String, Any>): List<TokenDto> {
        var assignee = ""
        var assigneeType = ""
        var tokenStatus = ""
        if (parameters["assignee"] != null) {
            assignee = parameters["assignee"].toString()
        }
        if (parameters["assigneeType"] != null) {
            assigneeType = parameters["assigneeType"].toString()
        }
        if (parameters["tokenStatus"] != null) {
            tokenStatus = parameters["tokenStatus"].toString()
        }
        val tokenEntities = tokenRepository.findTokenMstEntityByAssigneeIdAndAssigneeTypeAndTokenStatus(assignee, assigneeType, tokenStatus)
        val tokenDtoList: MutableList<TokenDto> = mutableListOf()
        for (tokenEntity in tokenEntities) {
            val tokenDto = TokenDto(
                    tokenId = tokenEntity.tokenId,
                    elementId = tokenEntity.elementId,
                    tokenStatus = tokenEntity.tokenStatus,
                    assigneeId = tokenEntity.assigneeId,
                    assigneeType = tokenEntity.assigneeType,
                    documentId = tokenEntity.instance.document.documentId,
                    documentName = tokenEntity.instance.document.documentName
            )
            tokenDtoList.add(tokenDto)
        }

        return tokenDtoList
    }

    /**
     * Token + Instance Save.
     *
     * @param tokenSaveDto
     * @return Boolean
     */
    fun postTokenData(tokenSaveDto: TokenSaveDto): Boolean {
        val documentDto = documentRepository.findDocumentEntityByDocumentId(tokenSaveDto.documentDto.documentId)
        val processId = documentDto.process.processId
        val instanceDto = InstanceDto(instanceId = "", document = documentDto)
        val instance = wfInstanceService.createInstance(instanceDto)
        tokenSaveDto.tokenDto.elementId =  wfElementService.getElementId(processId, ElementConstants.ElementStatusType.START.value).elementId
        val token = createToken(instance, tokenSaveDto.tokenDto)
        createTokenData(tokenSaveDto.tokenDto, instance.instanceId, token.tokenId)
        tokenSaveDto.tokenDto.tokenId = token.tokenId
        if (tokenSaveDto.tokenDto.isComplete) {
            completeToken(tokenSaveDto)
        }
        return true
    }

    /**
     * Token View.
     *
     * @param tokenId
     * @return TokenDto
     */
    fun getToken(tokenId: String): TokenDto {
        val tokenEntity = tokenRepository.findTokenEntityByTokenId(tokenId)
        val tokenDataEntities = tokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val componentList: MutableList<TokenDataDto> = mutableListOf()
        for (tokenDataEntity in tokenDataEntities) {
            val tokenDataDto = TokenDataDto(
                    componentId = tokenDataEntity.componentId,
                    value = tokenDataEntity.value
            )
            componentList.add(tokenDataDto)
        }

        return TokenDto(
                tokenId = tokenEntity.get().tokenId,
                elementId = tokenEntity.get().elementId,
                assigneeType = tokenEntity.get().assigneeType,
                assigneeId = tokenEntity.get().assigneeId,
                tokenStatus = tokenEntity.get().tokenStatus,
                isComplete = tokenEntity.get().tokenStatus == TokenConstants.Status.FINISH.code,
                documentId = tokenEntity.get().instance.document.documentId,
                documentName = tokenEntity.get().instance.document.documentName,
                data = componentList
        )
    }

    /**
     * Token Update.
     *
     * @param tokenDto
     */
    fun putToken(tokenDto: TokenDto): Boolean {
        val tokenEntity = tokenRepository.findTokenEntityByTokenId(tokenDto.tokenId)
        val instanceId = tokenEntity.get().instance.instanceId
        updateToken(tokenEntity.get(), tokenDto)
        deleteTokenData(instanceId, tokenDto.tokenId)
        createTokenData(tokenDto, instanceId, tokenDto.tokenId)
        return true
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return TokenViewDto
     */
    fun getTokenData(tokenId: String): TokenViewDto {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

        val tokenMstEntity = tokenRepository.findTokenEntityByTokenId(tokenId)
        val componentEntities = tokenMstEntity.get().instance.document.form.components
        val tokenDataEntities = tokenDataRepository.findTokenDataEntityByTokenId(tokenId)

        val componentList: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        if (componentEntities != null) {
            for (componentEntity in componentEntities) {

                //attributes
                val attributes = LinkedHashMap<String, Any>()
                val common = LinkedHashMap<String, Any>()
                common["mapping-id"] = componentEntity.mappingId
                attributes["type"] = componentEntity.componentType
                attributes["common"] = common

                for (attribute in componentEntity.attributes!!) {
                    val element = JsonParser().parse(attribute.attributeValue)
                    when (element.isJsonArray) {
                        true -> attributes[attribute.attributeId] = mapper.readValue(attribute.attributeValue, mapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java))
                        false -> attributes[attribute.attributeId] = mapper.readValue(attribute.attributeValue, LinkedHashMap::class.java)
                    }
                }

                //values
                val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
                for (tokenDataEntity in tokenDataEntities) {
                    if (tokenDataEntity.componentId == componentEntity.componentId) {
                        val valueMap = LinkedHashMap<String, Any>()
                        valueMap["value"] = tokenDataEntity.value
                        values.add(valueMap)
                    }
                }

                val component = LinkedHashMap<String, Any>()
                component["componentId"] = componentEntity.componentId
                component["attributes"] = attributes
                component["values"] = values
                componentList.add(component)
            }
        }

        val componentsMap = LinkedHashMap<String, Any>()
        componentsMap["components"] = componentList

        //action
        val actionDto = ActionDto(
                name = "",
                value = ""
        )

        return TokenViewDto(
                tokenId = tokenMstEntity.get().tokenId,
                components = componentList,
                action = actionDto
        )
    }

    /**
     * Token + Instance Update.
     *
     * @param tokenSaveDto
     * @return Boolean
     */
    fun putTokenData(tokenSaveDto: TokenSaveDto): Boolean {
        val tokenEntity = tokenRepository.findTokenEntityByTokenId(tokenSaveDto.tokenDto.tokenId)
        if (tokenEntity.isPresent) {
            val instanceId = tokenEntity.get().instance.instanceId
            updateToken(tokenEntity.get(), tokenSaveDto.tokenDto)
            deleteTokenData(instanceId, tokenSaveDto.tokenDto.tokenId)
            createTokenData(tokenSaveDto.tokenDto, instanceId, tokenSaveDto.tokenDto.tokenId)
            if (tokenSaveDto.tokenDto.isComplete) {
                completeToken(tokenSaveDto)
            }
        }
        return true
    }

    /**
     * Token Create.
     *
     * @param tokenDto
     * @return TokenEntity
     */
    fun createToken(instance: InstanceEntity, tokenDto: TokenDto): TokenEntity {
        val tokenEntity = TokenEntity(
                tokenId = "",
                elementId = tokenDto.elementId,
                tokenStatus = TokenConstants.Status.RUNNING.code,
                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                instance = instance
        )
        return tokenRepository.save(tokenEntity)
    }

    /**
     * Token Data Insert.
     *
     * @param tokenSaveDto
     * @param instanceId
     * @param tokenId
     */
    fun createTokenData(tokenDto: TokenDto, instanceId: String, tokenId: String) {
        val tokenDataEntities: MutableList<TokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenDto.data!!) {
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
     * @param tokenDto
     */
    fun updateToken(tokenEntity: TokenEntity, tokenDto: TokenDto) {
        tokenEntity.assigneeId = tokenDto.assigneeId
        tokenEntity.assigneeType = tokenDto.assigneeType
        tokenRepository.save(tokenEntity)
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
                        data = null,
                        documentId = completedToken.instance.document.documentId,
                        documentName = completedToken.instance.document.documentName
                )

                when (it.elementType) {
                    ElementConstants.ElementType.USER_TASK.value -> {
                        val createdToken = createToken(completedToken.instance, nextToken)
                        createTokenData(tokenSaveDto.tokenDto, completedToken.instance.instanceId, createdToken.tokenId)
                    }
                    ElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                        val createdToken = createToken(completedToken.instance, nextToken)
                        createTokenData(tokenSaveDto.tokenDto, completedToken.instance.instanceId, createdToken.tokenId)
                        completeToken(TokenSaveDto(tokenSaveDto.documentDto,  nextToken))
                    }
                    ElementConstants.ElementType.COMMON_END_EVENT.value -> {
                        wfInstanceService.completeInstance(completedToken.instance.instanceId)
                    }
                    else -> {}
                }
            }
        }
    }

    /**
     * Get Token Assignee.
     *
     * @param assigneeData
     * @return String
     */
    private fun getAssigneeForToken(assigneeData: String?): String {
        // 데이터가 만약 mappingExpresion('${xxxx}'와 같은) 이라면 문서에서 해당 코드로 매핑된 데이터를 찾아서 사용.
        // 데이터가 단순히 문자열이라면 그대로 사용.
        var assigneeForToken: String = ""

        val mappingExpr = TokenConstants.mappingExpression.toRegex()
        assigneeData?.let {
            assigneeForToken = if (mappingExpr.matches(assigneeData)) it.substring (2, assigneeData.length-1) else assigneeData
        }
        return assigneeForToken
    }

}
