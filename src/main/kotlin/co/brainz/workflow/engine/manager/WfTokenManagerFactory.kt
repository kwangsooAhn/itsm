package co.brainz.workflow.engine.manager

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.impl.WfCommonEndEventTokenManager
import co.brainz.workflow.engine.manager.impl.WfCommonStartEventTokenManager
import co.brainz.workflow.engine.manager.impl.WfExclusiveGatewayTokenManager
import co.brainz.workflow.engine.manager.impl.WfManualTaskTokenManager
import co.brainz.workflow.engine.manager.impl.WfSignalSendTokenManager
import co.brainz.workflow.engine.manager.impl.WfSubProcessTokenManager
import co.brainz.workflow.engine.manager.impl.WfUserTaskTokenManager
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import org.springframework.stereotype.Component

@Component
class WfTokenManagerFactory(
    private val wfTokenManagerService: WfTokenManagerService
) {

    fun getTokenManager(elementType: String): WfTokenManager {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                WfCommonStartEventTokenManager(wfTokenManagerService)
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                WfCommonEndEventTokenManager(wfTokenManagerService)
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                WfUserTaskTokenManager(wfTokenManagerService)
            }
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                WfManualTaskTokenManager(wfTokenManagerService)
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                WfSubProcessTokenManager(wfTokenManagerService)
            }
            WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                WfSignalSendTokenManager(wfTokenManagerService)
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                WfExclusiveGatewayTokenManager(wfTokenManagerService)
            }
            else -> throw AliceException(AliceErrorConstants.ERR, "Element tokenManager not found.")
        }
    }
}
