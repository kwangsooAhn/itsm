package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - task 엘리먼트
 *
 */
class WfProcessSimulationTask(wfElementRepository: WfElementRepository, wfProcessRepository: WfProcessRepository) :
    WfProcessSimulationElement(wfElementRepository, wfProcessRepository) {
    override fun validate(element: WfElementEntity): Boolean {
        return true
    }

    override fun failInfo(): String {
        return "Task process simulation failed. $elementInformation"
    }
}
