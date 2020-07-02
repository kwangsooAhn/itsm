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
            val connectorType: Int
            val arrowConnectorElementAttributeId: String
            when (sourceElement.getElementDataValue(WfElementConstants.AttributeId.CONDITION_ITEM.value)) {
                WfElementConstants.AttributeValue.ACTION.value -> {
                    connectorType = WfElementConstants.ConnectorConditionValue.ACTION.value
                    arrowConnectorElementAttributeId = WfElementConstants.AttributeId.ACTION_VALUE.value
                }
                else -> {
                    connectorType = WfElementConstants.ConnectorConditionValue.CONDITION.value
                    arrowConnectorElementAttributeId = WfElementConstants.AttributeId.CONDITION_VALUE.value
                }
            }

            // 체크된 기본경로 값을 담을 변수
            var checkedDefaultConditionTotal = 0
            // 현재 connector 값
            val currentConnectorValue = element.getElementDataValue(arrowConnectorElementAttributeId) ?: ""
            when (connectorType) {
                WfElementConstants.ConnectorConditionValue.ACTION.value -> {
                    // action-name과 action-value는 쌍으로 있어야 하므로 2개 중 하나의 값만 있으면 안된다.
                    val isName =
                        element.getElementDataValue(WfElementConstants.AttributeId.ACTION_NAME.value)?.isBlank()
                            ?: true
                    val isValue =
                        element.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)?.isBlank()
                            ?: true
                    if (!(!isName && !isValue)) {
                        return setFailedMessage("Check the action name and value.")
                    }
                }
                WfElementConstants.ConnectorConditionValue.CONDITION.value -> {
                    // connector 에 기본경로가 체크되어 있는지 확인하고 체크되어 있지 않은 경우 값이 존재하는지 확인한다.
                    checkedDefaultConditionTotal = checkedDefaultConditionCount(element, checkedDefaultConditionTotal)
                    if (checkedDefaultConditionTotal == 0 && currentConnectorValue.isBlank()) {
                        return setFailedMessage("Connector value is empty.")
                    }
                }
            }

            // 현재 connector 는 제외한 나머지 connector 를 구한다.
            val otherConnectors = arrowConnectors.filter {
                it.elementId != element.elementId
            }
            otherConnectors.forEach {
                // 현재 connector 값과 나머지 connector 값이 중복되었는지 확인한다.
                val otherConnectorValue = it.getElementDataValue(arrowConnectorElementAttributeId) ?: ""
                if (currentConnectorValue == otherConnectorValue) {
                    return setFailedMessage("Connector value was duplicated.")
                }
                // 체크된 기본경로가 있는지 확인하여 카운팅한다.
                checkedDefaultConditionTotal = checkedDefaultConditionCount(it, checkedDefaultConditionTotal)
            }

            // 체크된 기본경로 값은 0 또는 1개여야한다. (action은 무조건 pass)
            if (checkedDefaultConditionTotal > 1) {
                return setFailedMessage("Default connector checked should be one. If action then all checks should be unchecked.")
            }
        }
        return true
    }

    override fun failInfo(): String {
        return "Arrow connector simulation failed. $elementInformation"
    }

    /**
     * connector[element] 에 체크된 기본경로가 존재하는지 확인하여 현재 체크되어 있는 기본경로총개수[checkedDefaultConditionTotal]에 합산해서 리턴
     */
    private fun checkedDefaultConditionCount(element: WfElementEntity, checkedDefaultConditionTotal: Int): Int {
        var count = checkedDefaultConditionTotal
        val enabled = element.getElementDataValue(WfElementConstants.AttributeId.IS_DEFAULT.value)
        if (enabled == WfElementConstants.AttributeValue.IS_DEFAULT_ENABLE.value) {
            count++
        }
        return count
    }
}
