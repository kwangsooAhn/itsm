/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.process.dto.SimulationReport
import co.brainz.workflow.process.dto.SimulationReportDto
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.process.service.simulation.element.impl.WfProcessSimulationArrow
import co.brainz.workflow.process.service.simulation.element.impl.WfProcessSimulationEvent
import co.brainz.workflow.process.service.simulation.element.impl.WfProcessSimulationGateway
import co.brainz.workflow.process.service.simulation.element.impl.WfProcessSimulationSubProcess
import co.brainz.workflow.process.service.simulation.element.impl.WfProcessSimulationTask
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
    private lateinit var simulationReportDto: SimulationReportDto
    private lateinit var completeElements: MutableSet<String>

    /**
     * [processId] 에 해당하는 프로세스 시뮬레이션을 실행한다.
     */
    fun getProcessSimulation(processId: String): SimulationReportDto {
        logger.info("Simulation start...")

        val process = wfProcessRepository.getOne(processId)
        val elementEntities = process.elementEntities
        elementEntities.forEach { data ->
            if (data.processEntity == null) {
                data.processEntity = process
            }
        }

        this.run(elementEntities)

        logger.info(
            "Process message - {}\nSimulation validate - All validation complete.",
            this.processMessage.toString()
        )
        return simulationReportDto
    }

    /**
     * 엘리먼트를 하나씩 찾아가면서 이상유무를 확인한다
     */
    private fun run(elementEntities: MutableList<WfElementEntity>) {
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

            // GW 에 의한 분기 진행이 종료될 때 처리 (종료 or GW 다른 화살표)
            if (gatewayQueue.size > 0 &&
                currentElement.elementType == WfElementConstants.ElementType.COMMON_END_EVENT.value
            ) {
                currentElement = gatewayQueue.pop()
            }

            // 회수, 반려에 의해 앞으로 넘어온 경우 무한 반복을 해제
            if (completeElements.contains(currentElement.elementId)) {
                if (gatewayQueue.size > 0) {
                    currentElement = gatewayQueue.pop()
                } else {
                    break
                }
            }
            // GW 제외한 element 중 처리한 element 는 저장한다. (GW에 의한 반려, 회수 시 제외)
            if (WfElementConstants.ElementType.getAtomic(currentElement.elementType) != WfElementConstants.ElementType.GATEWAY) {
                completeElements.add(currentElement.elementId)
            }
        }

        // 전체 엘리먼트와 검사된 엘리먼트의 개수를 비교한다.
        // arrowConnector의 start-id, end-id 를 가지고 연결된 라인이므로 잘 그려진 프로세스이면 사이즈는 항상 같다.
        if (allElementEntitiesInProcess.size != removedDuplicationElements.size) {
            logger.error("Simulation failed. size error.")
            throw AliceException(AliceErrorConstants.ERR_00005, "Simulation failed. size error.")
        }
    }

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
        this.simulationReportDto = SimulationReportDto()
        this.completeElements = mutableSetOf()
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
                    this.gatewayQueue.push(current)
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
    private fun simulation(element: WfElementEntity) {
        this.removedDuplicationElements.add(element)

        val simulationElement = when (WfElementConstants.ElementType.getAtomic(element.elementType)) {
            WfElementConstants.ElementType.EVENT -> WfProcessSimulationEvent(
                wfDocumentRepository
            )
            WfElementConstants.ElementType.ARROW_CONNECTOR -> WfProcessSimulationArrow(
                wfElementRepository
            )
            WfElementConstants.ElementType.GATEWAY -> WfProcessSimulationGateway(
                wfElementRepository
            )
            WfElementConstants.ElementType.SUB_PROCESS -> WfProcessSimulationSubProcess(
                wfDocumentRepository
            )
            WfElementConstants.ElementType.TASK -> WfProcessSimulationTask()
            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                "Not found simulation. Check simulation factory class."
            )
        }

        val result = simulationElement.validation(element)

        this.simulationReportDto.addSimulationReport(
            SimulationReport(
                result,
                element,
                simulationElement.getFailedMessage()
            )
        )

        processMessage.append("\n").append(element.elementType).append("-")
            .append(element.elementId)
            .append("(")
            .append(element.getElementDataValue(WfElementConstants.AttributeId.NAME.value))
            .append(")")
    }
}
