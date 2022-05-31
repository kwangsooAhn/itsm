/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.process.service.simulation.element.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - task 엘리먼트
 *
 */
class WfProcessSimulationTask(private val wfElementDataRepository: WfElementDataRepository) : WfProcessSimulationElement() {
    override fun validate(element: WfElementEntity): Boolean {
        val arrowConnector =
            wfElementDataRepository.countByAttributeIdAndAttributeValue(WfElementConstants.AttributeId.SOURCE_ID.value, element.elementId)
        if (arrowConnector == 0) {
            return failed("ArrowConnector element not found.")
        }
        return true
    }

    override fun failedMessage(): String {
        return "Task process simulation failed. $simulationFailedMsg"
    }
}
