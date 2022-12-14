/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.impl.WfCommonEndEvent
import co.brainz.workflow.engine.manager.impl.WfCommonStartEvent
import co.brainz.workflow.engine.manager.impl.WfExclusiveGateway
import co.brainz.workflow.engine.manager.impl.WfManualTask
import co.brainz.workflow.engine.manager.impl.WfScriptTask
import co.brainz.workflow.engine.manager.impl.WfSignalSend
import co.brainz.workflow.engine.manager.impl.WfSubProcess
import co.brainz.workflow.engine.manager.impl.WfUserTask
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import org.springframework.stereotype.Component

@Component
class WfTokenManagerFactory(
    private val wfTokenManagerService: WfTokenManagerService
) {

    fun createTokenManager(tokenDto: WfTokenDto): WfTokenManager {
        return when (tokenDto.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                WfCommonStartEvent(wfTokenManagerService)
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                WfCommonEndEvent(wfTokenManagerService)
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                WfUserTask(wfTokenManagerService)
            }
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                WfManualTask(wfTokenManagerService)
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                WfSubProcess(wfTokenManagerService)
            }
            WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                WfSignalSend(wfTokenManagerService)
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                WfExclusiveGateway(wfTokenManagerService)
            }
            WfElementConstants.ElementType.SCRIPT_TASK.value -> {
                val tokenManager = WfScriptTask(wfTokenManagerService)
                val element = wfTokenManagerService.getElement(tokenDto.elementId)
                run loop@{
                    element.elementDataEntities.forEach { data ->
                        if (data.attributeId == WfElementConstants.AttributeId.AUTO_COMPLETE.value) {
                            if (data.attributeValue == "N") {
                                tokenManager.isAutoComplete = false
                            }
                            return@loop
                        }
                    }
                }
                return tokenManager
            }
            else -> throw AliceException(AliceErrorConstants.ERR, "Element tokenManager not found.")
        }
    }
}
