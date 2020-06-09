package co.brainz.workflow.process.service.simulation.element.impl

import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - task 엘리먼트
 *
 */
class WfProcessSimulationTask : WfProcessSimulationElement() {
    override fun validate(element: WfElementEntity): Boolean {
        return true
    }

    override fun failInfo(): String {
        return "Task process simulation failed. $elementInformation"
    }
}
