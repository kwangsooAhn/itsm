package co.brainz.workflow.engine.manager

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.impl.WfCommonEndEventTokenManager
import co.brainz.workflow.engine.manager.impl.WfCommonStartEventTokenManager
import co.brainz.workflow.engine.manager.impl.WfManualTaskTokenManager
import co.brainz.workflow.engine.manager.impl.WfUserTaskTokenManager
import org.springframework.stereotype.Component

@Component
class WfTokenManagerFactory(
    private val constructorManager: ConstructorManager
) {

    fun getTokenManager(elementType: String): WfTokenManager {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                WfCommonStartEventTokenManager(constructorManager)
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                WfCommonEndEventTokenManager(constructorManager)
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                WfUserTaskTokenManager(constructorManager)
            }
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                WfManualTaskTokenManager(constructorManager)
            }
            else -> throw AliceException(AliceErrorConstants.ERR, "Element tokenManager not found.")
        }
    }
}
