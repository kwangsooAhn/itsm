package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - event (start, end)
 *
 * start 또는 end 이벤트는 1개만 존재해야한다.
 */
class WfProcessSimulationEvent() : WfProcessSimulationElement() {

    override fun validate(element: WfElementEntity): Boolean {
        val process = element.processEntity
        val findElements =
            process?.elementEntities?.filter {
                it.elementType == element.elementType
            }

        val isOnlyOne = findElements?.size == 1
        if (!isOnlyOne) {
            setFailedMessage("There should be only one start/end event.")
        }

        return isOnlyOne
    }

    override fun failInfo(): String {
        return "Event simulation failed. $elementInformation"
    }
}
