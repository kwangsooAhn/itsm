package co.brainz.workflow.process.service.simulation.element

import co.brainz.workflow.element.entity.WfElementDataEntity
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.process.constants.WfProcessConstants
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
     * 시뮬레이션시 통과시킬 수 있는 상태값을 확인하여 boolean값 리턴.
     */
    protected fun checkProcessStatus(status: String): Boolean {
        val checkStatus = arrayListOf(
            WfProcessConstants.Status.PUBLISH.code,
            WfProcessConstants.Status.USE.code,
            WfFormConstants.FormStatus.PUBLISH.value,
            WfFormConstants.FormStatus.USE.value
        )
        return checkStatus.contains(status)
    }

    /**
     * 엘리먼트데이터[elementData]의 필수값을 검증한다.
     *
     * @return Boolean
     */
    private fun requiredValueVerification(elementData: List<WfElementDataEntity>): Boolean {
        elementData.forEach {
            if (it.attributeRequired && it.attributeValue.isEmpty()) {
                return setFailedMessage("Required value is empty.")
            }
        }
        return true
    }

    /**
     * 공통 메서드
     */
    fun validation(element: WfElementEntity): Boolean {
        val elementId = element.elementId
        val elementName = element.elementName
        logger.info("Simulation validate - ElementId:{}, ElementName:{}", elementId, elementName)
        elementInformation = "<br>ElementId: $element.elementId <br>ElementName: ${element.elementName}}"

        if (!validate(element)) {
            return false
        }

        return this.requiredValueVerification(element.elementDataEntities)
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
