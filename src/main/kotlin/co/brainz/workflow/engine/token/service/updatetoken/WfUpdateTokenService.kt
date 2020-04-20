package co.brainz.workflow.engine.token.service.updatetoken

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * 엘리먼트에 해당하는 토큰을 업데이트
 */
@Service
class WfUpdateTokenService(
    private val wfTokenRepository: WfTokenRepository,
    private val wfActionService: WfActionService,
    private val wfUpdateCommonToken: WfUpdateCommonToken,
    private val wfUpdateUserTaskToken: WfUpdateUserTaskToken,
    private val wfUpdateSignalToken: WfUpdateSignalToken
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun updateTokenAction(wfTokenDto: WfTokenDto) {
        val wfTokenEntity = wfTokenRepository.findTokenEntityByTokenId(wfTokenDto.tokenId).get()
        val wfElementEntity = wfActionService.getElement(wfTokenEntity.element.elementId)
        logger.debug("Token Element Type : {}", wfElementEntity.elementType)
        val tokenAction = when (wfTokenDto.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> wfUpdateCommonToken
            WfElementConstants.ElementType.USER_TASK.value -> wfUpdateUserTaskToken
            WfElementConstants.ElementType.SIGNAL_EVENT.value -> wfUpdateSignalToken
            else -> throw AliceException(AliceErrorConstants.ERR_00005, "Not found element token action.")
        }
        tokenAction.updateToken(wfTokenEntity, wfElementEntity, wfTokenDto)
    }


}
