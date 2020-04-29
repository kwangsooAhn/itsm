package co.brainz.workflow.engine.process.service.simulation

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.springframework.stereotype.Service
import java.util.ArrayDeque

/**
 * 프로세스 시뮬레이션을 위한 엘리먼트 연결.
 *
 * 프로세스 이벤트 시작 ~ 종료 까지 엘리먼트를 연결짓는다.
 */
@Service
class WfProcessSimulationMaker {

    private lateinit var connectedElements: MutableList<WfElementEntity>
    private lateinit var gatewaySnapshot: ArrayDeque<MutableList<WfElementEntity>>
    private lateinit var elementEntities: MutableList<WfElementEntity>

    /**
     * 초기화.
     * 전체 엘리먼트를 저장하고 사용할 변수들을 초기화한다.
     */
    fun init(elementEntities: MutableList<WfElementEntity>) {
        this.connectedElements = mutableListOf()
        this.gatewaySnapshot = ArrayDeque()
        this.elementEntities = elementEntities
    }

    /**
     * [elementType] 이 일치하는 엔티티를 리턴한다.
     */
    private fun getElement(elementType: String): WfElementEntity {
        return this.elementEntities.first {
            it.elementType == elementType
        }
    }

    /**
     * [current] 엘리먼트 엔티티 id 와 ElementDataEntity의 data
     */
    private fun getElementEntityById(current: WfElementEntity, attributeId: String): WfElementEntity {
        return this.elementEntities.first {
            current.elementId == it.getElementDataValue(attributeId)
        }
    }

    /**
     * 엘리먼트를 서로 연결한다. (시작 ~ 종료까지)
     *
     */
    private fun connectElement(element: WfElementEntity) {
        connectedElements.add(element)
    }

    /**
     * 시뮬레이션을 위한 엘리먼트를 연결하여 리턴한다.
     */
    fun getElements(): MutableList<WfElementEntity> {
        // start
        val start = this.getElement(WfElementConstants.ElementType.COMMON_START_EVENT.value)
        this.connectElement(start)

        // end
        val end = this.getElement(WfElementConstants.ElementType.COMMON_END_EVENT.value)

        var current = start
        while (current.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {
            // 컨넥터 가져오기.
            val connector = this.getElementEntityById(current, WfElementConstants.ElementType.COMMON_START_EVENT.value)
            this.connectElement(connector)

            // 다음 엘리먼트 가져오기
            val element = this.getElementEntityById(current, WfElementConstants.ElementType.COMMON_END_EVENT.value)
            this.connectElement(element)

            current = element

        }

        return connectedElements
    }

}
