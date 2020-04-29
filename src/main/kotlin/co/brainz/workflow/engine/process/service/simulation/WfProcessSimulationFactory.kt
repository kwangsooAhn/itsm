package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationGateway
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationTask
import org.springframework.stereotype.Service

/**
 * 시뮬레이션 검증 팩토리 클래스
 */
@Service
class WfProcessSimulationFactory {
    fun getProcessSimulation(elementType: String): WfProcessSimulationElement {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> WfProcessSimulationTask()
            WfElementConstants.ElementType.ARROW_CONNECTOR.value -> WfProcessSimulationTask()
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> WfProcessSimulationGateway()
            WfElementConstants.ElementType.SUB_PROCESS.value -> WfProcessSimulationTask()
            WfElementConstants.ElementType.USER_TASK.value -> WfProcessSimulationTask()
            else -> throw AliceException(AliceErrorConstants.ERR_00005, AliceErrorConstants.ERR_00005.message)
        }
    }
}
