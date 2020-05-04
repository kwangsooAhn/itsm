package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.repository.WfElementRepository
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
    private val documentRepository: WfDocumentRepository
) {
    fun getProcessSimulation(elementType: String): WfProcessSimulationElement {
        return when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value,
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> WfProcessSimulationEvent()

            WfElementConstants.ElementType.ARROW_CONNECTOR.value -> WfProcessSimulationArrow(elementRepository)

            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value,
            WfElementConstants.ElementType.PARALLEL_GATEWAY.value,
            WfElementConstants.ElementType.INCLUSIVE_GATEWAY.value -> WfProcessSimulationGateway(elementRepository)

            WfElementConstants.ElementType.SUB_PROCESS.value -> WfProcessSimulationSubProcess(documentRepository)

            WfElementConstants.ElementType.USER_TASK.value,
            WfElementConstants.ElementType.MANUAL_TASK.value,
            WfElementConstants.ElementType.SEND_TASK.value,
            WfElementConstants.ElementType.RECEIVE_TASK.value,
            WfElementConstants.ElementType.SCRIPT_TASK.value -> WfProcessSimulationTask()

            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                "Not found simulation. Check simulation factory class."
            )
        }
    }
}
