package co.brainz.workflow.engine.token.service.updatetoken

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.engine.token.service.WfTokenActionService

/**
 * Common Event 토큰을 업데이트한다.
 */
class WfUpdateCommonToken(
    private val wfTokenActionService: WfTokenActionService,
    wfActionService: WfActionService,
    wfTokenRepository: WfTokenRepository,
    wfInstanceService: WfInstanceService,
    wfElementService: WfElementService,
    wfTokenDataRepository: WfTokenDataRepository,
    wfDocumentRepository: WfDocumentRepository
) : WfUpdateTokenService(
    wfTokenActionService,
    wfActionService,
    wfTokenRepository,
    wfInstanceService,
    wfElementService,
    wfTokenDataRepository,
    wfDocumentRepository
) {

    override fun updateToken(wfTokenEntity: WfTokenEntity, wfElementEntity: WfElementEntity, wfTokenDto: WfTokenDto) {
        logger.debug("Token Action : {}", wfTokenDto.action)
        when (wfTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, wfTokenDto)
                goToNext(wfTokenEntity, wfElementEntity, wfTokenDto)
            }
        }
    }
}
