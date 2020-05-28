package co.brainz.workflow.engine.manager

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.impl.WfCommonEndEventTokenManager
import co.brainz.workflow.engine.manager.impl.WfCommonStartEventTokenManager
import co.brainz.workflow.engine.manager.impl.WfUserTaskTokenManager
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.token.repository.WfCandidateRepository
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import org.springframework.stereotype.Component

@Component
class WfTokenManagerFactory(
    private val wfElementService: WfElementService,
    private val wfInstanceService: WfInstanceService,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfCandidateRepository: WfCandidateRepository
) {

    fun getTokenManager(elementType: String): WfTokenManager {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                WfCommonStartEventTokenManager(
                    wfElementService,
                    wfInstanceService,
                    wfInstanceRepository,
                    wfElementRepository,
                    wfTokenRepository,
                    wfTokenDataRepository,
                    wfCandidateRepository
                )
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                WfCommonEndEventTokenManager(
                    wfElementService,
                    wfInstanceService,
                    wfInstanceRepository,
                    wfElementRepository,
                    wfTokenRepository,
                    wfTokenDataRepository,
                    wfCandidateRepository
                )
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                WfUserTaskTokenManager(
                    wfElementService,
                    wfInstanceService,
                    wfInstanceRepository,
                    wfElementRepository,
                    wfTokenRepository,
                    wfTokenDataRepository,
                    wfCandidateRepository
                )
            }
            else -> throw AliceException(AliceErrorConstants.ERR, "Element tokenManager not found.")
        }
    }
}
