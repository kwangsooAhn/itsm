package co.brainz.workflow.engine.process.service.simulation.element

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import org.slf4j.LoggerFactory

/**
 * 시뮬레이션 엘리먼트 추상 클래스
 */
abstract class WfProcessSimulationElement(
    protected val wfElementRepository: WfElementRepository,
    protected val wfProcessRepository: WfProcessRepository
) {

    protected val logger = LoggerFactory.getLogger(this::class.java)
    protected var elementInformation = ""

    /**
     * 에러시 공통 처리
     */
    protected fun setFailedMessage(failedMessage: String) {
        elementInformation = "$failedMessage $elementInformation"
        logger.error("{}", elementInformation)
    }

    /**
     * 공통 메서드
     */
    fun getValidation(element: WfElementEntity): Boolean {
        val elementId = element.elementId
        val attrId = element.getElementDataValue("id")
        val attrName = element.getElementDataValue("name")
        logger.info("Simulation validate - ElementId:{}, attrId:{}, attrName:{}", elementId, attrId, attrName)
        elementInformation = "<br>AttrId: " + element.elementId + "<br>AttrName: " + element.getElementDataValue("name")
        return validate(element)
    }

    /**
     * 엘리먼트 검증하기
     */
    abstract fun validate(element: WfElementEntity): Boolean

    /**
     * 엘리먼트 검증 실패시 관련 정보 리턴하기
     */
    abstract fun failInfo(): String
}
