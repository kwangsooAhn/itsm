package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getSimulation(processId: String): Boolean {
        logger.info("Simulation start")

        val process = wfProcessRepository.getOne(processId)
        val elementEntities = process.elementEntities

        // 초기화 후 시뮬레이션을 위한 엘리먼트 연결하기
        wfProcessSimulationMaker.init(elementEntities)
        val simulationElements = wfProcessSimulationMaker.getElements()

        // 엘리먼트 검증하기
        simulationElements.forEach {
            val simulator = wfProcessSimulationFactory.getProcessSimulation(it.elementType)
            val result = simulator.getValidation(it)

            if (!result) {
                logger.info("Simulation failed. throw exception.")
                throw AliceException(AliceErrorConstants.ERR_00005, simulator.failInfo())
            }
        }

        logger.info("Simulation end.")
        return true
    }
}
