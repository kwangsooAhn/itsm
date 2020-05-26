package co.brainz.workflow.engine.process.service.simulation.element

import co.brainz.workflow.engine.element.entity.WfElementEntity
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 시뮬레이션 엘리먼트 추상 클래스
 */
abstract class WfProcessSimulationElement {

    protected val logger: Logger = LoggerFactory.getLogger(this::class.java)
    protected var elementInformation = ""

    /**
     * 에러시 공통 처리
     */
    protected fun setFailedMessage(failedMessage: String): Boolean {
        elementInformation = "$failedMessage $elementInformation"
        logger.error("{}", elementInformation)
        return false
    }

    /**
     * 공통 메서드
     */
    fun validation(element: WfElementEntity): Boolean {
        val elementId = element.elementId
        val elementName = element.elementName
        logger.info("Simulation validate - ElementId:{}, ElementName:{}", elementId, elementName)
        elementInformation = "<br>ElementId: $element.elementId <br>ElementName: ${element.elementName}}"
        return if (commonValidate(element)) {
            validate(element)
        } else {
            false
        }
    }

    /**
     * Common Validate (Required value).
     *
     * @param element
     * @return Boolean
     */
    private fun commonValidate(element: WfElementEntity): Boolean {
        var validate = true
        element.elementDataEntities.forEach { elementData ->
            if (elementData.attributeRequired && elementData.attributeValue.isEmpty()) {
                validate = false
                setFailedMessage("Required value is empty.")
                return@forEach
            }
        }
        return validate
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
