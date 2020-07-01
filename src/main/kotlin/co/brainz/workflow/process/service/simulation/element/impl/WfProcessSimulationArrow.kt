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
        val sourceElement = wfElementRepository.findWfElementEntityByElementId(sourceElementId!!)
        val arrowConnectors = wfElementRepository.findAllArrowConnectorElement(sourceElement.elementId)
        val sourceElementIsGateway =
            WfElementConstants.ElementType.getAtomic(sourceElement.elementType) == WfElementConstants.ElementType.GATEWAY

        if (sourceElementIsGateway && arrowConnectors.size > 1) {
            val arrowConnectorElementAttributeId =
                when (sourceElement.getElementDataValue(WfElementConstants.AttributeId.CONDITION_ITEM.value)) {
                    WfElementConstants.AttributeValue.ACTION.value -> WfElementConstants.AttributeId.ACTION_VALUE.value
                    else -> WfElementConstants.AttributeId.CONDITION_VALUE.value
                }

            val arrowConnectorValues: MutableList<String?> = mutableListOf()
            arrowConnectors.forEach {
                val arrowConnectorValue = it.getElementDataValue(arrowConnectorElementAttributeId) ?: ""

                // connector 값이 있는지 확인한다.
                if (arrowConnectorValue.isBlank()) {
                    return setFailedMessage("Connector value is empty.")
                }

                // connector 값이 중복되었는지 확인한다.
                if (arrowConnectorValues.contains(arrowConnectorValue)) {
                    return setFailedMessage("Connector value was duplicated.")
                }
                arrowConnectorValues.add(arrowConnectorValue)

                // #{action} 이면 action-name과 action-value는 쌍으로 있어야 하며 2개 중 하나의 값만 있으면 안된다.
                if (arrowConnectorElementAttributeId == WfElementConstants.AttributeId.ACTION_VALUE.value) {
                    val isName =
                        element.getElementDataValue(WfElementConstants.AttributeId.ACTION_NAME.value)?.isBlank() ?: true
                    val isValue =
                        element.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)?.isBlank()
                            ?: true
                    if ((isName && !isValue) || (!isName && isValue)) {
                        return setFailedMessage("Check the action name and value .")
                    }
                }
            }
        }
        return true
    }

    override fun failInfo(): String {
        return "Arrow connector simulation failed. $elementInformation"
    }
}
