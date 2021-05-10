/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.impl

import co.brainz.framework.util.AliceUtil
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.provider.constants.RestTemplateConstants

class WfSubProcess(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = false
) : WfTokenManager(wfTokenManagerService) {

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        // Set mapping component data.
        val element = wfTokenManagerService.getElement(createTokenDto.elementId)
        val documentId =
            super.getAttributeValue(
                element.elementDataEntities,
                WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
            )
        val startTokenDto = createTokenDto.copy()
        startTokenDto.documentId = documentId
        startTokenDto.parentTokenId = startTokenDto.tokenId
        val makeDocumentTokens =
            wfTokenManagerService.makeMappingTokenDto(super.tokenEntity, mutableListOf(documentId))
        makeDocumentTokens.forEach {
            it.assigneeId = createTokenDto.assigneeId
            it.instanceId = AliceUtil().getUUID()
            it.instancePlatform = createTokenDto.instancePlatform ?: RestTemplateConstants.InstancePlatform.ITSM.code
            WfEngine(wfTokenManagerService).startWorkflow(it)
        }

        super.suspendToken(createTokenDto)

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        super.tokenEntity.assigneeId =
            wfTokenManagerService.getCurrentAssigneeForChildProcess(completedToken.tokenId) ?: completedToken.assigneeId
        wfTokenManagerService.saveToken(super.tokenEntity)
        return completedToken
    }
}
