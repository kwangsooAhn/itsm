package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.element.constants.WfElementConstants
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
        logger.info("Simulation start...")

        val process = wfProcessRepository.getOne(processId)
        val elementEntities = process.elementEntities

        // 시뮬레이션을 위한 엘리먼트 연결한다.
        wfProcessSimulationMaker.init(elementEntities)
        val simulations = wfProcessSimulationMaker.getElements()

        if (logger.isDebugEnabled) {
            simulations.forEach { simulationElements ->
                simulationElements.forEach {
                    logger.debug(
                        "Simulation elements - {}:{}:{}",
                        it.elementType,
                        it.getElementDataValue(WfElementConstants.AttributeId.ID.value),
                        it.getElementDataValue(WfElementConstants.AttributeId.NAME.value)
                    )
                }
            }
        }

        // 연결된 시뮬레이션 라인과 총 엘리먼트의 개수를 비교한다.
        // arrowConnector의 start-id, end-id 를 가지고 연결된 라인이므로 잘 그려진 프로세스이면 사이즈는 항상 같다.
        if (elementEntities.size != wfProcessSimulationMaker.getElementSize()) {
            logger.error("Simulation failed. size error.")
            throw AliceException(AliceErrorConstants.ERR_00005, "Simulation failed. size error.")
        }
        logger.info("Simulation validate - Element size success.")

        // 시뮬레이션 라인을 하나씩 검증한다.
        simulations.forEach { simulationElements ->
            simulationElements.forEach {
                val simulator = wfProcessSimulationFactory.getProcessSimulation(it.elementType)
                val result = simulator.getValidation(it)

                if (!result) {
                    logger.info("Simulation failed. throw exception.")
                    throw AliceException(AliceErrorConstants.ERR_00005, simulator.failInfo())
                }
            }
        }
        logger.info("Simulation validate - All validation complete.")

        logger.info("Simulation end.")
        return true
    }
}
