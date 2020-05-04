package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationArrow
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationEvent
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationGateway
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationSubProcess
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationTask
import org.springframework.stereotype.Service

/**
 * 시뮬레이션 검증 팩토리 클래스
 */
@Service
class WfProcessSimulationFactory(
    private val elementRepository: WfElementRepository,
    private val processRepository: WfProcessRepository
) {
    fun getProcessSimulation(elementType: String): WfProcessSimulationElement {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value,
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> WfProcessSimulationEvent(
                elementRepository,
                processRepository
            )
            WfElementConstants.ElementType.ARROW_CONNECTOR.value -> WfProcessSimulationArrow(
                elementRepository,
                processRepository
            )
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> WfProcessSimulationGateway(
                elementRepository,
                processRepository
            )
            WfElementConstants.ElementType.SUB_PROCESS.value -> WfProcessSimulationSubProcess(
                elementRepository,
                processRepository
            )
            WfElementConstants.ElementType.USER_TASK.value -> WfProcessSimulationTask(
                elementRepository,
                processRepository
            )
            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                "Not found simulation. Check simulation factory class."
            )
        }
    }
}
