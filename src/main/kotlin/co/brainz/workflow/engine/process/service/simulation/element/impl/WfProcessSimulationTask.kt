package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - task 엘리먼트
 *
 * 엘리먼트의 in, out 화살표가 1개씩만 존재한다.
 */
class WfProcessSimulationTask(elementRepository: WfElementRepository) : WfProcessSimulationElement(elementRepository) {
    override fun validate(element: WfElementEntity): Boolean {





        return false
    }

    override fun failInfo(): String {
        TODO("Not yet implemented")
    }
}
