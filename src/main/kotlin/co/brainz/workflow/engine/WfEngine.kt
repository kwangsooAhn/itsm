/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDataDto
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenAction
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
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

        val instance = wfTokenManagerService.createInstance(tokenDto)
        val element = wfTokenManagerService.getStartElement(instance.document.process.processId)
        tokenDto.instanceId = instance.instanceId
        tokenDto.elementType = element.elementType
        tokenDto.elementId = element.elementId

        // Start Token Create
        val tokenManager = this.createTokenManager(tokenDto.elementType)
        var startTokenDto = tokenManager.createToken(tokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // First Token Create
        val firstTokenDto = tokenManager.createNextToken(startTokenDto)

        return this.progressWorkflow(firstTokenDto)
    }

    /**
     * Progress workflow.
     */
    fun progressWorkflow(tokenDto: WfTokenDto): Boolean {
        logger.debug("Progress Token")
        when (tokenDto.action) {
            WfElementConstants.Action.PROGRESS.value -> {
                var progressTokenDto = tokenDto.copy()
                do {
                    progressTokenDto = this.getTokenDto(progressTokenDto)
                    val tokenManager = this.createTokenManager(progressTokenDto.elementType)
                    progressTokenDto = tokenManager.completeToken(progressTokenDto)
                    progressTokenDto = tokenManager.createNextToken(progressTokenDto)
                } while (tokenManager.isAutoComplete)
            }
            else -> WfTokenAction(wfTokenManagerService).action(tokenDto)
        }

        return true
    }

    /**
     * Get TokenDto Init.
     *  - 진행중 인 문서의 경우 토큰 조회 후 newToken 생성
     */
    private fun getTokenDto(tokenDto: WfTokenDto): WfTokenDto {
        val newToken = tokenDto.copy()
        val tokenEntity = wfTokenManagerService.getToken(tokenDto.tokenId)
        newToken.instanceId = tokenEntity.instance.instanceId
        newToken.elementType = tokenEntity.element.elementType
        newToken.elementId = tokenEntity.element.elementId

        return newToken
    }

    /**
     * Get TokenManager.
     */
    private fun createTokenManager(elementType: String): WfTokenManager {
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(elementType)
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
