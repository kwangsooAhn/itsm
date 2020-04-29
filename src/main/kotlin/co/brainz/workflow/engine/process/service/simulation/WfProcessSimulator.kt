package co.brainz.workflow.engine.process.service.simulation

import co.brainz.workflow.engine.process.repository.WfProcessRepository
import org.springframework.stereotype.Service

/**
 * 프로세스 시뮬레이션
 */
@Service
class WfProcessSimulator(
    private val wfProcessRepository: WfProcessRepository,
    private val wfProcessSimulationMaker: WfProcessSimulationMaker,
    private val wfProcessSimulationFactory: WfProcessSimulationFactory
) {

    fun getSimulation(processId: String) { // }: WfElementEntity {

        val process = wfProcessRepository.getOne(processId)
        val elementEntities = process.elementEntities

        // 초기화하고 시뮬레이션을 위한 엘리먼트 연결하기
        wfProcessSimulationMaker.init(elementEntities)
        val simulationElements = wfProcessSimulationMaker.getElements()

        // 엘리먼트 검증하기
        simulationElements.forEach {
            val simulator = wfProcessSimulationFactory.getProcessSimulation(it.elementType)
            simulator.getValidation(it)
        }

    }
}
