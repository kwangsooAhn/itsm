package co.brainz.workflow.engine.token.service

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.dto.WfTokenDataDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.dto.WfTokenViewDto
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.mapper.WfTokenMapper
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.mapstruct.factory.Mappers
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.ZoneId

@Service
@Transactional
class WfTokenService(
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfInstanceService: WfInstanceService,
    private val wfElementService: WfElementService,
    private val wfFormService: WfFormService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val tokenMapper = Mappers.getMapper(WfTokenMapper::class.java)

    /**
     * Search Tokens.
     *
     * @param parameters
     * @return List<LinkedHashMap<String, Any>>
     */
    fun getTokens(parameters: LinkedHashMap<String, Any>): List<LinkedHashMap<String, Any>> {
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
        val tokenEntities = wfTokenRepository.findTokenMstEntityByAssigneeIdAndAssigneeTypeAndTokenStatus(
            assignee,
            assigneeType,
            tokenStatus
        )
        val returnValue: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

        for (tokenEntity in tokenEntities) {
            val tokenDto = WfTokenDto(
                tokenId = tokenEntity.tokenId,
                elementId = tokenEntity.elementId,
                tokenStatus = tokenEntity.tokenStatus,
                assigneeId = tokenEntity.assigneeId,
                assigneeType = tokenEntity.assigneeType,
                documentId = tokenEntity.instance.document.documentId,
                documentName = tokenEntity.instance.document.documentName
            )

            val tokenMap = LinkedHashMap<String, Any>()
            tokenMap["token"] = tokenDto
            returnValue.add(tokenMap)
        }

        return returnValue
    }

    /**
     * Token + Instance Save.
     *
     * @param wfTokenDto
     * @return Boolean
     */
    fun postTokenData(wfTokenDto: WfTokenDto): Boolean {
        val documentDto = wfTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val processId = documentDto?.process?.processId
        val instanceDto = documentDto?.let { WfInstanceDto(instanceId = "", document = it) }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }
        // TODO
        // start element는 몇 종류가 있는데 1개의 프로세스에 commonStart는 1개만 존재하지만 다른 종류의 start는 복수개가 존재가능.
        // 이에 대한 처리를 위해 getStartElement에 파라미터가 추가될 필요가 있다.
        wfTokenDto.elementId = processId?.let { wfElementService.getStartElement(it).elementId }.toString()
        val token = instance?.let { createToken(it, wfTokenDto) }
        if (instance != null && token != null) {
            createTokenData(wfTokenDto, token.tokenId)
            wfTokenDto.tokenId = token.tokenId
        }
        if (wfTokenDto.isComplete) {
            completeToken(wfTokenDto)
        }
        return true
    }

    /**
     * Token View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getToken(tokenId: String): LinkedHashMap<String, Any> {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
        val componentList: MutableList<WfTokenDataDto> = mutableListOf()
        for (tokenDataEntity in tokenDataEntities) {
            val tokenDataDto = WfTokenDataDto(
                componentId = tokenDataEntity.componentId,
                value = tokenDataEntity.value
            )
            componentList.add(tokenDataDto)
        }

        val tokenDto = WfTokenDto(
            tokenId = tokenEntity.get().tokenId,
            elementId = tokenEntity.get().elementId,
            assigneeType = tokenEntity.get().assigneeType,
            assigneeId = tokenEntity.get().assigneeId,
            tokenStatus = tokenEntity.get().tokenStatus,
            isComplete = tokenEntity.get().tokenStatus == WfTokenConstants.Status.FINISH.code,
            documentId = tokenEntity.get().instance.document.documentId,
            documentName = tokenEntity.get().instance.document.documentName,
            data = componentList
        )

        val returnValue = LinkedHashMap<String, Any>()
        returnValue["token"] = tokenDto

        return returnValue
    }

    /**
     * Token Update.
     *
     * @param wfTokenDto
     */
    fun putToken(wfTokenDto: WfTokenDto): Boolean {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId)
        updateToken(tokenEntity.get(), wfTokenDto)
        deleteTokenData(wfTokenDto.tokenId)
        createTokenData(wfTokenDto, wfTokenDto.tokenId)
        return true
    }

    /**
     * Token + Instance View.
     *
     * @param tokenId
     * @return LinkedHashMap<String, Any>
     */
    fun getTokenData(tokenId: String): LinkedHashMap<String, Any> {
        val tokenMstEntity = wfTokenRepository.findTokenEntityByTokenId(tokenId)
        val componentEntities = tokenMstEntity.get().instance.document.form.components
        val tokenDataEntities = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)

        val componentList: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        if (componentEntities != null) {
            for (componentEntity in componentEntities) {
                val attributes = wfFormService.makeAttributes(componentEntity)
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

        val tokenViewDto = WfTokenViewDto(
            tokenId = tokenMstEntity.get().tokenId,
            components = componentList,
            action = wfElementService.getActionList(tokenMapper.toWfTokenDto(tokenMstEntity.get()))
        )

        val returnValue = LinkedHashMap<String, Any>()
        returnValue["token"] = tokenViewDto

        return returnValue
    }

    /**
     * Token + Instance Update.
     *
     * @param wfTokenDto
     * @return Boolean
     */
    fun putTokenData(wfTokenDto: WfTokenDto): Boolean {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId)
        if (tokenEntity.isPresent) {
            updateToken(tokenEntity.get(), wfTokenDto)
            deleteTokenData(wfTokenDto.tokenId)
            createTokenData(wfTokenDto, wfTokenDto.tokenId)
            if (wfTokenDto.isComplete) {
                completeToken(wfTokenDto)
            }
        }

        return true
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
                tokenStatus = WfTokenConstants.Status.RUNNING.code,
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

    /**
     * Token Complete.
     *
     * @param wfTokenDto
     */
    fun completeToken(wfTokenDto: WfTokenDto) {

        logger.debug("=============== COMPLTE TOKEN ===============")

        // Optional -> 안쓰면 좋을 것 같은데...
        val completedTokenOptional = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId)

        if (completedTokenOptional.isPresent) {
            val completedToken = completedTokenOptional.get()

            // 토큰 완료 처리
            completedToken.tokenStatus = WfTokenConstants.Status.FINISH.code
            completedToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            wfTokenRepository.save(completedToken)

            //  다음 Element 가져오기
            val nextElement: WfElementEntity? = wfElementService.getNextElement(wfTokenDto)

            logger.debug("Next element {}", nextElement)
            nextElement?.let { it ->
                val assigneeValueInNextElement: String? =
                    it.getElementDataValue(WfElementConstants.AttributeId.ASSIGNEE.value)
                val assigneeTypeValueInNextElement: String? =
                    it.getElementDataValue(WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)

                val nextToken = WfTokenDto(
                    tokenId = "",
                    isComplete = false,
                    elementId = it.elementId,
                    elementType = it.elementType,
                    assigneeId = getAssigneeForToken(assigneeValueInNextElement),
                    assigneeType = assigneeTypeValueInNextElement,
                    data = null,
                    documentId = completedToken.instance.document.documentId,
                    documentName = completedToken.instance.document.documentName
                )

                when (it.elementType) {
                    WfElementConstants.ElementType.USER_TASK.value -> {
                        logger.debug("Element type: USER_TASK")
                        val createdToken = createToken(completedToken.instance, nextToken)
                        createTokenData(wfTokenDto, createdToken.tokenId)
                    }
                    WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                        logger.debug("Element type: EXCLUSIVE_GATEWAY")
                        val createdToken = createToken(completedToken.instance, nextToken)
                        val createdTokenDataDto = mutableListOf<WfTokenDataDto>()
                        val createdTokenData = wfTokenDataRepository.findTokenDataEntityByTokenId(createdToken.tokenId)
                        createdTokenData.forEach { tokenData ->
                            createdTokenDataDto.add(
                                WfTokenDataDto(
                                    componentId = tokenData.componentId,
                                    value = tokenData.value
                                )
                            )
                        }
                        nextToken.tokenId = createdToken.tokenId
                        nextToken.data = createdTokenDataDto
                        createTokenData(wfTokenDto, createdToken.tokenId)
                        this.completeToken(nextToken)
                    }
                    WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                        logger.debug("Element type: COMMON_END_EVENT")
                        wfInstanceService.completeInstance(completedToken.instance.instanceId)
                    }
                    else -> {
                        logger.debug("Element type: NONE")
                    }
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
        var assigneeForToken = ""

        val mappingExpr = WfTokenConstants.mappingExpression.toRegex()
        assigneeData?.let {
            assigneeForToken =
                if (mappingExpr.matches(assigneeData)) it.substring(2, assigneeData.length - 1) else assigneeData
        }
        return assigneeForToken
    }

}
