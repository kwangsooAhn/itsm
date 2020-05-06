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
        return when (WfElementConstants.ElementType.getAtomic(elementType)) {
            WfElementConstants.ElementType.EVENT -> WfProcessSimulationEvent()
            WfElementConstants.ElementType.ARROW_CONNECTOR -> WfProcessSimulationArrow(elementRepository)
            WfElementConstants.ElementType.GATEWAY -> WfProcessSimulationGateway(elementRepository)
            WfElementConstants.ElementType.SUB_PROCESS -> WfProcessSimulationSubProcess(documentRepository)
            WfElementConstants.ElementType.TASK -> WfProcessSimulationTask()
            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                "Not found simulation. Check simulation factory class."
            )
        }
    }
}
