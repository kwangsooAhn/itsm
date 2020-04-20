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
import org.springframework.stereotype.Service

/**
 * User Task 토큰 업데이트
 */
@Service
class WfUpdateUserTaskToken(
    wfTokenActionService: WfTokenActionService,
    wfActionService: WfActionService,
    wfTokenRepository: WfTokenRepository,
    wfInstanceService: WfInstanceService,
    wfElementService: WfElementService,
    wfTokenDataRepository: WfTokenDataRepository,
    wfDocumentRepository: WfDocumentRepository

) : WfUpdateToken(
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
            WfElementConstants.Action.SAVE.value -> {
                wfTokenDto.assigneeId = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
                wfTokenActionService.save(wfTokenEntity, wfTokenDto)
            }
            WfElementConstants.Action.REJECT.value -> {
                val values = HashMap<String, Any>()
                values[WfElementConstants.AttributeId.REJECT_ID.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.REJECT_ID.value
                )
                values[WfElementConstants.AttributeId.ASSIGNEE_TYPE.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
                )
                values[WfElementConstants.AttributeId.ASSIGNEE.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
                wfTokenActionService.setReject(wfTokenEntity, wfTokenDto, values)
            }
            WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.setWithdraw(wfTokenEntity, wfTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, wfTokenDto)
                goToNext(wfTokenEntity, wfElementEntity, wfTokenDto)
            }
        }
    }
}
