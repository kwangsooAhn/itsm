package co.brainz.workflow.engine

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.ConstructorManager
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
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

    fun startWorkflow(wfTokenDto: WfTokenDto): Boolean {
        logger.debug("Start Workflow")

        // Create Instance
        val instance = wfInstanceService.createInstance(wfTokenDto)

        // StartToken Create & Complete
        val element = wfElementService.getStartElement(instance.document.process.processId)
        var startTokenDto = wfTokenDto
        startTokenDto.instanceId = instance.instanceId
        startTokenDto.elementType = element.elementType
        startTokenDto.elementId = element.elementId
        val tokenManager = getTokenManager(startTokenDto.elementType)
        startTokenDto = tokenManager.createToken(startTokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // FirstToken Create
        val firstTokenDto = tokenManager.createNextToken(startTokenDto)

        return progressWorkflow(firstTokenDto)
    }

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
                    tokenManager.completeToken(tokenDto) //현재 토큰 종료처리
                    token = tokenManager.createNextToken(tokenDto) //현재 토큰의 next에서 다음 token을 찾은 후 createToken 호출
                } while (tokenDto.isAutoComplete) // 해당 토큰이 반복해서 실행되어야 하는 경우 (종료 토큰 생성후 반복하여 endDt 찍고 false 변경 종료)
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

    private fun setTokenDtoValue(token: WfTokenDto): WfTokenDto {
        //val token = wfTokenDto.copy()
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
