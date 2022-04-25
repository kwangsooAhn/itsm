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
import javax.transaction.Transactional
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
        logger.info("Start Workflow : {}", tokenDto.documentName)

        // 참조인, 댓글, 태그 입력시 임시로 등록된 데이터가 존재할 경우가 있으므로 삭제한 후 다시 시작 이벤트를 생성한다.
        wfTokenManagerService.deleteTokenByInstanceId(tokenDto.instanceId)
        // 시작 이벤트 이후 첫번째 토큰 생성.
        val firstTokenDto = this.getFirstToken(tokenDto)

        return this.progressWorkflow(firstTokenDto!!)
    }

    /**
     * Get First Token.
     */
    @Transactional
    fun getFirstToken(tokenDto: WfTokenDto): WfTokenDto? {
        val instance = wfTokenManagerService.createInstance(tokenDto)
        val element = wfTokenManagerService.getStartElement(instance.document.process.processId)
        tokenDto.instanceId = instance.instanceId
        tokenDto.elementType = element.elementType
        tokenDto.elementId = element.elementId

        // 시작 이벤트 생성 및 완료 처리.
        val tokenManager = this.createTokenManager(tokenDto)
        var startTokenDto = tokenManager.createToken(tokenDto)
        startTokenDto = tokenManager.completeToken(startTokenDto)

        // 시작 이벤트 이후 첫번째 토큰 생성.
        return tokenManager.createNextToken(startTokenDto)
    }

    /**
     * Progress workflow.
     */
    fun progressWorkflow(tokenDto: WfTokenDto): Boolean {
        logger.debug("Progress Token")

        if (tokenDto.action == null) {
            logger.warn("Token action data is null.")
            return false
        }

        when (WfElementConstants.Action.isApplicationAction(tokenDto.action!!)) {
            true -> this.actionProgress(tokenDto)
            else -> {
                var isFirst = true // 최초 실행에 대한 옵션 (ex: 처리시 본인 참조인 자동 읽음 처리 등)
                // 프로세스로 그려진 동적인 흐름을 진행하는 동작.
                var currentTokenDto = tokenDto.copy()
                var currentTokenManager: WfTokenManager
                var nextTokenDto: WfTokenDto?
                var nextTokenManager: WfTokenManager
                do {
                    currentTokenDto = this.getTokenDto(currentTokenDto)
                    currentTokenManager = this.createTokenManager(currentTokenDto)
                    nextTokenDto = this.currentTokenCompleteAndCreateNextToken(currentTokenManager, currentTokenDto, isFirst)
                    if (nextTokenDto != null) {
                        // 다음 토큰 생성 후 추가작업 (필요한 경우)
                        nextTokenManager = this.createTokenManager(nextTokenDto)
                        nextTokenManager.nextTokenOptionProcessing(nextTokenDto)
                        currentTokenDto = nextTokenDto
                    } else { // 다음 토큰이 없으면 종료.
                        nextTokenManager = currentTokenManager
                        nextTokenManager.isAutoComplete = false
                    }
                    isFirst = false
                } while (nextTokenManager.isAutoComplete)
            }
        }
        return true
    }

    /**
     * 버튼 액션 처리
     */
    @Transactional
    fun actionProgress(tokenDto: WfTokenDto) {
        WfTokenAction(wfTokenManagerService).progressApplicationAction(tokenDto)
    }

    /**
     * 현재 토큰 종료 + 다음 토큰 생성 후 DB 반영
     */
    @Transactional
    fun currentTokenCompleteAndCreateNextToken(
        currentTokenManager: WfTokenManager,
        currentTokenDto: WfTokenDto,
        isFirst: Boolean
    ): WfTokenDto? {
        if (isFirst) {
            // 참조인 처리 (문서 담당자와 참조인이 동일할때 문서 처리와 동시에 읽음처리)
            if (currentTokenDto.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {
                WfTokenAction(wfTokenManagerService).actionReview(currentTokenDto)
            }
        }
        val completedTokenDto = currentTokenManager.completeToken(currentTokenDto)
        return currentTokenManager.createNextToken(completedTokenDto)
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
            instanceId = restTemplateTokenDto.instanceId,
            fileDataIds = restTemplateTokenDto.fileDataIds,
            assigneeId = restTemplateTokenDto.assigneeId,
            data = tokenData,
            action = restTemplateTokenDto.action,
            parentTokenId = restTemplateTokenDto.parentTokenId,
            instancePlatform = restTemplateTokenDto.instancePlatform
        )
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
    private fun createTokenManager(tokenDto: WfTokenDto): WfTokenManager {
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(tokenDto)
    }
}
