package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationArrow
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationEvent
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationGateway
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationSubProcess
import co.brainz.workflow.engine.process.service.simulation.element.impl.WfProcessSimulationTask
import java.util.ArrayDeque
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * 프로세스 시뮬레이션
 */
@Service
class WfProcessSimulator(
    private val wfProcessRepository: WfProcessRepository,
    private val wfElementRepository: WfElementRepository,
    private val wfDocumentRepository: WfDocumentRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var gatewayQueue: ArrayDeque<WfElementEntity>
    private lateinit var arrowConnectorInGateway: MutableMap<String, ArrayDeque<WfElementEntity>>
    private lateinit var allElementEntitiesInProcess: List<WfElementEntity>
    private lateinit var removedDuplicationElements: MutableSet<WfElementEntity>
    private lateinit var processMessage: StringBuilder

    /**
     * 전체 엘리먼트를 저장하고 사용할 변수들을 초기화.
     */
    private fun initialize(elementEntities: MutableList<WfElementEntity>) {
        this.gatewayQueue = ArrayDeque()
        this.arrowConnectorInGateway = mutableMapOf()
        // 시뮬레이션할 엘리먼트중 artifact 는 제외
        this.allElementEntitiesInProcess = elementEntities.filter {
            WfElementConstants.ElementType.getAtomic(it.elementType) != WfElementConstants.ElementType.ARTIFACT
        }
        this.removedDuplicationElements = mutableSetOf()
        this.processMessage = StringBuilder()
    }

    /**
     * 시작 엘리먼트를 리턴.
     */
    private fun getStartElement(): WfElementEntity {
        lateinit var element: WfElementEntity
        try {
            element = this.allElementEntitiesInProcess.first {
                it.elementType == WfElementConstants.ElementType.COMMON_START_EVENT.value
            }
        } catch (e: NoSuchElementException) {
            throw AliceException(AliceErrorConstants.ERR_00005, "Start element not found.")
        }
        return element
    }

    /**
     * [current] 엘리먼트를 start-id 로 가지는 arrowConnector 를 리턴.
     */
    private fun getArrowElement(current: WfElementEntity): WfElementEntity {
        lateinit var element: WfElementEntity
        try {
            val arrowConnectors = this.allElementEntitiesInProcess.filter {
                current.elementId == it.getElementDataValue(WfElementConstants.AttributeId.SOURCE_ID.value)
            }
            // gateway 면
            if (current.elementType == WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value) {
                if (arrowConnectorInGateway[current.elementId] == null) {
                    arrowConnectorInGateway[current.elementId] = ArrayDeque(arrowConnectors)
                }
                element = arrowConnectorInGateway[current.elementId]!!.pop()

                // 모두 꺼내서 사용을 했으면 해당 gateway 는 삭제.
                if (arrowConnectorInGateway[current.elementId]!!.size == 0) {
                    arrowConnectorInGateway.remove(current.elementId)
                } else {
                    // 검증해야할 gateway 발 arrowConnector가 존재하므로 또다시 꺼낼 수 있도록 큐에 넣어 둔다.
                    gatewayQueue.push(current)
                }
            } else {
                element = arrowConnectors.last()
            }
        } catch (e: NoSuchElementException) {
            throw AliceException(AliceErrorConstants.ERR_00005, "ArrowConnector element not found.")
        }
        return element
    }

    /**
     *  엘리먼트[arrowConnector]의 end-id를 가지는 엘리먼트를 리턴.
     */
    private fun getTargetElement(arrowConnector: WfElementEntity): WfElementEntity {
        val targetElementId = arrowConnector.getElementDataValue(WfElementConstants.AttributeId.TARGET_ID.value)
            ?: throw AliceException(AliceErrorConstants.ERR_00005, "ArrowConnector end-id is null.")

        return allElementEntitiesInProcess.first {
            it.elementId == targetElementId
        }
    }

    /**
     * 엘리먼트 종류 마다 검증해야 할 내용 시뮬레이션 결과 리턴
     *
     * 점검할 엘리먼트들[removedDuplicationElements]은 별도로 저장한다.
     */
    private fun simulation(element: WfElementEntity): Boolean {
        removedDuplicationElements.add(element)

        val simulationElement = when (WfElementConstants.ElementType.getAtomic(element.elementType)) {
            WfElementConstants.ElementType.EVENT -> WfProcessSimulationEvent()
            WfElementConstants.ElementType.ARROW_CONNECTOR -> WfProcessSimulationArrow(wfElementRepository)
            WfElementConstants.ElementType.GATEWAY -> WfProcessSimulationGateway(wfElementRepository)
            WfElementConstants.ElementType.SUB_PROCESS -> WfProcessSimulationSubProcess(wfDocumentRepository)
            WfElementConstants.ElementType.TASK -> WfProcessSimulationTask()
            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                "Not found simulation. Check simulation factory class."
            )
        }

        val result = simulationElement.validation(element)

        if (!result) {
            logger.info("Simulation failed.")
            throw AliceException(AliceErrorConstants.ERR_00005, simulationElement.failInfo())
        }

        processMessage.append("\n").append(element.elementType).append("-")
            .append(element.elementId)
            .append("(")
            .append(element.getElementDataValue(WfElementConstants.AttributeId.NAME.value))
            .append(")")

        return result
    }

    /**
     * 엘리먼트를 하나씩 찾아가면서 이상유무를 확인한다
     */
    private fun run(elementEntities: MutableList<WfElementEntity>): Boolean {
        this.initialize(elementEntities)

        // 시작 엘리먼트 찾기.
        val startElement = this.getStartElement()
        this.simulation(startElement)

        var currentElement = startElement
        while (currentElement.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {

            // 컨넥터 가져오기.
            val arrowElement = this.getArrowElement(currentElement)
            this.simulation(arrowElement)

            // 다음 엘리먼트 가져오기
            currentElement = this.getTargetElement(arrowElement)
            this.simulation(currentElement)

            if (gatewayQueue.size > 0 &&
                currentElement.elementType == WfElementConstants.ElementType.COMMON_END_EVENT.value
            ) {
                currentElement = gatewayQueue.pop()
            }
        }

        // 전체 엘리먼트와 검사된 엘리먼트의 개수를 비교한다.
        // arrowConnector의 start-id, end-id 를 가지고 연결된 라인이므로 잘 그려진 프로세스이면 사이즈는 항상 같다.
        if (allElementEntitiesInProcess.size != removedDuplicationElements.size) {
            logger.error("Simulation failed. size error.")
            throw AliceException(AliceErrorConstants.ERR_00005, "Simulation failed. size error.")
        }

        return true
    }

    /**
     * 프로세스 처리간 저장한 메시지 출력
     */
    private fun getProcessMessage(): String {
        return this.processMessage.toString()
    }

    /**
     * [processId] 에 해당하는 프로세스 시뮬레이션을 실행한다.
     */
    fun getProcessSimulation(processId: String): Boolean {
        logger.info("Simulation start...")

        val process = wfProcessRepository.getOne(processId)
        val elementEntities = process.elementEntities

        this.run(elementEntities)

        logger.info(this.getProcessMessage())

        logger.info("Simulation validate - All validation complete.")
        return true
    }
}
