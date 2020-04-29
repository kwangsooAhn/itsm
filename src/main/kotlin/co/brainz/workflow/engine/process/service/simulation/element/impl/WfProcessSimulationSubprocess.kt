package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - subprocess
 */
class WfProcessSimulationSubprocess(elementRepository: WfElementRepository): WfProcessSimulationElement(elementRepository) {
    override fun validate(element: WfElementEntity): Boolean {
        // 서브프로세스 엘리먼트에는 발행상태인 프로세스가 등록되어 있어야 한다.
        // 서브프로세스는 in,out 화살표가 1개씩만 존재한다.
        TODO("Not yet implemented")
    }

    override fun failInfo(): String {
        TODO("Not yet implemented")
    }
}
