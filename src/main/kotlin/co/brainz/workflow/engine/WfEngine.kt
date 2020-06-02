package co.brainz.workflow.engine

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.entity.WfTokenDataEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfEngine(
    private val constructorManager: ConstructorManager
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val wfElementService = constructorManager.getElementService()
    private val wfInstanceService = constructorManager.getInstanceService()
    private val wfTokenRepository = constructorManager.getTokenRepository()
    private val wfTokenDataRepository = constructorManager.getTokenDataRepository()

    /**
     * Start workflow.
     *
     * @param wfTokenDto
     * @return Boolean
     */
    fun startWorkflow(wfTokenDto: WfTokenDto): Boolean {
        logger.debug("Start Workflow")

        // Create Instance
        val instance = wfInstanceService.createInstance(wfTokenDto)

        // StartToken Create & Complete
        val element = wfElementService.getStartElement(instance.document.process.processId)
        var startTokenDto = setTokenDtoInitValue(wfTokenDto, instance, element)
        val tokenManager = getTokenManager(startTokenDto.elementType)
        startTokenDto = tokenManager.createToken(startTokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // FirstToken Create
        val firstTokenDto = tokenManager.createNextToken(startTokenDto)

        return progressWorkflow(firstTokenDto)
    }

    /**
     * Progress workflow.
     *
     * @param wfTokenDto
     * @return Boolean
     */
    fun progressWorkflow(wfTokenDto: WfTokenDto): Boolean {
        logger.debug("Process Token")
        var token = wfTokenDto.copy()
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> {
                actionSave(wfTokenDto)
            }
            else -> {
                do {
                    val tokenDto = setTokenDtoValue(token)
                    val tokenManager = getTokenManager(tokenDto.elementType)
                    tokenManager.completeToken(tokenDto)
                    token = tokenManager.createNextToken(tokenDto)
                } while (tokenDto.isAutoComplete)
            }
        }

        return true
    }

    /**
     * Action - Save.
     *
     * @param wfTokenDto
     */
    private fun actionSave(wfTokenDto: WfTokenDto) {
        // Save Token & Token Data
        val token = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        token.assigneeId = wfTokenDto.assigneeId
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in wfTokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                tokenId = wfTokenDto.tokenId,
                componentId = tokenDataDto.componentId,
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            wfTokenDataRepository.saveAll(tokenDataEntities)
        }
        wfTokenRepository.save(token)
    }

    /**
     * Start workflow tokenDto init.
     *
     * @param wfTokenDto
     * @param instance
     * @param element
     * @return token
     */
    private fun setTokenDtoInitValue(
        wfTokenDto: WfTokenDto,
        instance: WfInstanceEntity,
        element: WfElementEntity
    ): WfTokenDto {
        val token = wfTokenDto.copy()
        token.instanceId = instance.instanceId
        token.elementType = element.elementType
        token.elementId = element.elementId
        token.documentId = instance.document.documentId
        return token
    }

    /**
     * Progress workflow tokenDto init.
     *
     * @param token
     * @return wfTokenDto
     */
    private fun setTokenDtoValue(token: WfTokenDto): WfTokenDto {
        val tokenEntity = wfTokenRepository.findTokenEntityByTokenId(token.tokenId).get()
        token.instanceId = tokenEntity.instance.instanceId
        token.elementType = tokenEntity.element.elementType
        token.elementId = tokenEntity.element.elementId
        token.documentId = tokenEntity.instance.document.documentId
        return token
    }

    /**
     * Get TokenManager.
     *
     * @param elementType
     * @return WfTokenManager
     */
    private fun getTokenManager(elementType: String): WfTokenManager {
        return WfTokenManagerFactory(constructorManager).getTokenManager(elementType)
    }

    /**
     * RestTemplateTokenDto To WfTokenDto.
     *
     * @param restTemplateTokenDto
     * @return WfTokenDto
     */
    fun toTokenDto(restTemplateTokenDto: RestTemplateTokenDto): WfTokenDto {
        return WfTokenDto(
            tokenId = restTemplateTokenDto.tokenId,
            documentId = restTemplateTokenDto.documentId,
            fileDataIds = restTemplateTokenDto.fileDataIds,
            assigneeId = restTemplateTokenDto.assigneeId,
            data = restTemplateTokenDto.data,
            action = restTemplateTokenDto.action,
            parentTokenId = restTemplateTokenDto.parentTokenId
        )
    }
}
