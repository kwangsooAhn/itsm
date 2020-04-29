package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - event (start, end)
 *
 * start 또는 end 이벤트는 1개만 존재해야한다.
 */
class WfProcessSimulationEvent(repository: WfElementRepository) : WfProcessSimulationElement(repository) {

    override fun validate(element: WfElementEntity): Boolean {
        val process = element.processEntity
        val findElements =
            process?.elementEntities?.filter {
                it.elementType == element.elementType
            }
        return findElements?.size == 1
    }

    override fun failInfo(): String {
        return "Event simulation failed"
    }
}
