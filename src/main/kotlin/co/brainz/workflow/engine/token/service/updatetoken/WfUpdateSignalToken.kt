package co.brainz.workflow.engine.token.service.updatetoken

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
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
 * 시그널 이벤트 토큰 업데이트
 */
class WfUpdateSignalToken(
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



    }
}
