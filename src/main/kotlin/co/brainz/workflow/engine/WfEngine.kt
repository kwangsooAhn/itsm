package co.brainz.workflow.engine

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDataDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.entity.WfTokenDataEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfEngine(
    private val wfTokenManagerService: WfTokenManagerService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Start workflow.
     */
    fun startWorkflow(tokenDto: WfTokenDto): Boolean {
        logger.debug("Start Workflow")

        var startTokenDto = this.initTokenDto(tokenDto)
        val tokenManager = this.getTokenManager(startTokenDto.elementType)
        startTokenDto = tokenManager.createToken(startTokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // First Token Create
        val firstTokenDto = tokenManager.createNextToken(startTokenDto)

        return this.progressWorkflow(firstTokenDto)
    }

    /**
     * Progress workflow.
     */
    fun progressWorkflow(tokenDto: WfTokenDto): Boolean {
        logger.debug("Process Token")
        var progressTokenDto = tokenDto.copy()
        when (tokenDto.action) {
            WfElementConstants.Action.SAVE.value -> this.actionSave(tokenDto)
            else -> {
                do {
                    progressTokenDto = this.initTokenDto(progressTokenDto)
                    val tokenManager = this.getTokenManager(progressTokenDto.elementType)
                    tokenManager.completeToken(progressTokenDto)
                    progressTokenDto = tokenManager.createNextToken(progressTokenDto)
                } while (progressTokenDto.isAutoComplete)
            }
        }

        return true
    }

    /**
     * Action - Save.
     */
    private fun actionSave(tokenDto: WfTokenDto) {
        // Save Token & Token Data
        val token = wfTokenManagerService.getToken(tokenDto.tokenId)
        token.assigneeId = tokenDto.assigneeId
        val tokenDataEntities: MutableList<WfTokenDataEntity> = mutableListOf()
        for (tokenDataDto in tokenDto.data!!) {
            val tokenDataEntity = WfTokenDataEntity(
                tokenId = tokenDto.tokenId,
                componentId = tokenDataDto.componentId,
                value = tokenDataDto.value
            )
            tokenDataEntities.add(tokenDataEntity)
        }
        if (tokenDataEntities.isNotEmpty()) {
            wfTokenManagerService.saveAllTokenData(tokenDataEntities)
        }
        wfTokenManagerService.saveToken(token)
    }

    /**
     * TokenDto Init.
     *  - 최초 신청서 작성시 tokenDto.documentId 값으로 instance 생성 후 nweToken 생성
     *  - 진행중 인 문서의 경우 토큰 조회 후 newToken 생성
     */
    private fun initTokenDto(tokenDto: WfTokenDto): WfTokenDto {
        val newToken = tokenDto.copy()
        when (tokenDto.tokenId) {
            "" -> {
                val instance = wfTokenManagerService.createInstance(tokenDto)
                val element = wfTokenManagerService.getStartElement(instance.document.process.processId)
                newToken.instanceId = instance.instanceId
                newToken.elementType = element.elementType
                newToken.elementId = element.elementId
            }
            else -> {
                val tokenEntity = wfTokenManagerService.getToken(tokenDto.tokenId)
                newToken.instanceId = tokenEntity.instance.instanceId
                newToken.elementType = tokenEntity.element.elementType
                newToken.elementId = tokenEntity.element.elementId
            }
        }

        return newToken
    }

    /**
     * Get TokenManager.
     */
    private fun getTokenManager(elementType: String): WfTokenManager {
        return WfTokenManagerFactory(wfTokenManagerService).getTokenManager(elementType)
    }

    /**
     * RestTemplateTokenDto To WfTokenDto.
     */
    fun toTokenDto(restTemplateTokenDto: RestTemplateTokenDto): WfTokenDto {
        val tokenData: MutableList<WfTokenDataDto> = mutableListOf()
        restTemplateTokenDto.data?.forEach { data ->
            tokenData.add(WfTokenDataDto(componentId = data.componentId, value = data.value))
        }
        return WfTokenDto(
            tokenId = restTemplateTokenDto.tokenId,
            documentId = restTemplateTokenDto.documentId,
            fileDataIds = restTemplateTokenDto.fileDataIds,
            assigneeId = restTemplateTokenDto.assigneeId,
            data = tokenData,
            action = restTemplateTokenDto.action,
            parentTokenId = restTemplateTokenDto.parentTokenId
        )
    }
}
