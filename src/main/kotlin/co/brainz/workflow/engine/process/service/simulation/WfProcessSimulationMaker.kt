package co.brainz.workflow.engine.process.service.simulation

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.ArrayDeque

/**
 * 프로세스 시뮬레이션을 위한 엘리먼트 연결.
 *
 * 프로세스 이벤트 시작 ~ 종료 까지 엘리먼트를 연결짓는다.
 */
@Service
class WfProcessSimulationMaker {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private lateinit var simulationElements: MutableList<MutableList<WfElementEntity>>
    private lateinit var connectedElements: MutableList<WfElementEntity>
    private lateinit var gatewaySnapshot: ArrayDeque<MutableList<WfElementEntity>>
    private lateinit var elementEntities: MutableList<WfElementEntity>
    private lateinit var removedDuplicationElements: MutableSet<WfElementEntity>

    /**
     * 초기화.
     * 전체 엘리먼트를 저장하고 사용할 변수들을 초기화한다.
     */
    fun init(elementEntities: MutableList<WfElementEntity>) {
        this.simulationElements = mutableListOf()
        this.connectedElements = mutableListOf()
        this.gatewaySnapshot = ArrayDeque()
        this.elementEntities = elementEntities
        this.removedDuplicationElements = mutableSetOf()
    }

    /**
     * 시뮬레이션을 위해 연결된 엘리먼트의 개수를 리턴한다.
     * 여러개의 시뮬레이션 라인 중 중복을 제거한 총 엘리먼트 개수이다.
     */
    fun getElementSize(): Int {
        return removedDuplicationElements.size
    }

    /**
     * 시뮬레이션을 위한 엘리먼트를 연결하여 리턴한다.
     */
    fun getElements(): MutableList<MutableList<WfElementEntity>> {

        logger.info("Get elements...")

        // start
        val startElement = this.getStartElement()
        this.connectElement(startElement)

        var currentElement = startElement
        while (currentElement.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {

            // 컨넥터 가져오기.
            val connectorElement = this.getArrowConnectElement(currentElement)
            this.connectElement(connectorElement)

            // 다음 엘리먼트 가져오기
            currentElement = this.getTargetElement(connectorElement)
            this.connectElement(currentElement)

            // 게이트웨이면 스냅샷 저장.
            if (currentElement.elementType == WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value) {
                gatewaySnapshot.push(this.connectedElements.toMutableList())
            }

            // 종료 이벤트면 현재까지 연결된 엘리먼트를 시뮬레이션 변수에 저장한다.
            // 게이트웨이로 인한 스냅샷이 있으면 그 위치부터 엘리먼트를 새로 연결한다.
            if (currentElement.elementType == WfElementConstants.ElementType.COMMON_END_EVENT.value) {
                simulationElements.add(this.connectedElements)
                if (gatewaySnapshot.size > 0) {
                    this.connectedElements = gatewaySnapshot.pop()
                    currentElement = this.connectedElements.last()
                }
            }
        }

        logger.info("Get elements end.")

        return simulationElements
    }

    /**
     * 시작 엘리먼트를 리턴한다.
     */
    private fun getStartElement(): WfElementEntity {
        lateinit var element: WfElementEntity
        try {
            element = this.elementEntities.first {
                it.elementType == WfElementConstants.ElementType.COMMON_START_EVENT.value
            }
        } catch (e: NoSuchElementException) {
            throw AliceException(AliceErrorConstants.ERR_00005, "Start element not found.")
        }
        return element
    }

    /**
     * 특정 프로세스의 전체 엘리먼트 중에서 현재[current] 엘리먼트를 start-id 로 가지는 arrowConnector 를 조회한다.
     */
    private fun getArrowConnectElement(current: WfElementEntity): WfElementEntity {
        lateinit var element: WfElementEntity
        try {
            element = this.elementEntities.first {
                current.elementId == it.getElementDataValue(WfElementConstants.AttributeId.SOURCE_ID.value) &&
                    !removedDuplicationElements.contains(it)
            }
        } catch (e: NoSuchElementException) {
            throw AliceException(AliceErrorConstants.ERR_00005, "ArrowConnector element not found.")
        }
        return element
    }

    /**
     * 특정 프로세스의 전체 엘리먼트 중에서 현재[arrowConnector] 엘리먼트의 end-id를 가지는 엘리먼트를 조회한다.
     */
    private fun getTargetElement(arrowConnector: WfElementEntity): WfElementEntity {
        val targetElementId = arrowConnector.getElementDataValue(WfElementConstants.AttributeId.TARGET_ID.value)
            ?: throw AliceException(AliceErrorConstants.ERR_00005, "ArrowConnector end-id is null.")

        return this.elementEntities.first {
            it.elementId == targetElementId
        }
    }

    /**
     * 엘리먼트를 서로 연결한다. (시작 ~ 종료까지)
     *
     */
    private fun connectElement(element: WfElementEntity) {
        connectedElements.add(element)
        removedDuplicationElements.add(element)
    }
}
