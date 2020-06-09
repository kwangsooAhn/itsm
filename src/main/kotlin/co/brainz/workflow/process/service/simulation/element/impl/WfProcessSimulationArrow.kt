package co.brainz.workflow.process.service.simulation.element.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - arrow connector
 */
class WfProcessSimulationArrow(private val wfElementRepository: WfElementRepository) : WfProcessSimulationElement() {

    override fun validate(element: WfElementEntity): Boolean {

        val sourceElementId = element.getElementDataValue(WfElementConstants.AttributeId.SOURCE_ID.value)
        val sourceElement = wfElementRepository.getOne(sourceElementId!!)
        val isGateway =
            WfElementConstants.ElementType.getAtomic(sourceElement.elementType) == WfElementConstants.ElementType.GATEWAY
        val isArrowConnectorSizeGreaterThanOne =
            wfElementRepository.findAllArrowConnectorElement(sourceElement.elementId).size > 1
        val emptyCondition =
            element.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)?.isBlank() ?: true

        // sourceElement가 gateWay 일 때는 connector의 개수가 2개 이상이면 condition-value 값이 있어야 한다.
        if (isGateway && isArrowConnectorSizeGreaterThanOne && emptyCondition) {
            return setFailedMessage("connector condition value is empty.")
        }

        // action name과 action value는 쌍으로 있어야 하며 2개중 하나의 값만 있으면 안된다.
        val isName = element.getElementDataValue(WfElementConstants.AttributeId.ACTION_NAME.value)?.isBlank() ?: true
        val isValue = element.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)?.isBlank() ?: true
        if ((isName && !isValue) || (!isName && isValue)) {
            return setFailedMessage("Check the action name and value .")
        }

        return true
    }

    override fun failInfo(): String {
        return "Arrow connector simulation failed. $elementInformation"
    }
}
