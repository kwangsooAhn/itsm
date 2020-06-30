package co.brainz.workflow.process.service.simulation.element.impl

import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - gateway
 */
class WfProcessSimulationGateway(private val wfElementRepository: WfElementRepository) : WfProcessSimulationElement() {
    override fun validate(element: WfElementEntity): Boolean {

        val arrowConnectors = wfElementRepository.findAllArrowConnectorElement(element.elementId)

        // gateway 의 arrowConnector 2개 이상이면
        if (arrowConnectors.size > 1) {

            // condition-item 은 값이 반드시 있어야 한다.
            val conditionItem = element.getElementDataValue(WfElementConstants.AttributeId.CONDITION_ITEM.value)
            val emptyCondition = conditionItem?.isBlank() ?: true
            if (emptyCondition) {
                return setFailedMessage("Condition item empty.")
            }

            // 컨넥터들의 종류가 condition인지 action인지 잘못 사용되고 있는지 확인한다.
            var connectorIsCondition = WfElementConstants.ConnectorConditionValue.NONE.value
            var connectorIsAction = WfElementConstants.ConnectorConditionValue.NONE.value
            // 컨넥터들의 종류를 확인하기 위해서 loop 도는 김에 중복된 값을 가졌는지 같이 확인한다.
            val conditionValues: MutableList<String?> = mutableListOf()
            val actionValues: MutableList<String?> = mutableListOf()
            var conditionValueWasDuplicated = false
            var actionValueWasDuplicated = false
            arrowConnectors.forEach {
                if (it.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)
                        ?.isNotBlank() == true
                ) {
                    connectorIsCondition = WfElementConstants.ConnectorConditionValue.CONDITION.value
                    // 컨넥터의 종류가 condition이면 condition-value 중복 미리 체크해둠.
                    val conditionValue =
                        it.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)
                    if (conditionValues.contains(conditionValue)) {
                        conditionValueWasDuplicated = true
                    }
                    conditionValues.add(conditionValue)
                }
                if (it.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)?.isNotBlank() == true) {
                    connectorIsAction = WfElementConstants.ConnectorConditionValue.ACTION.value
                    // 컨넥터의 종류가 action이면 action-value 중복 미리 체크해둠
                    val actionValue =
                        it.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)
                    if (actionValues.contains(actionValue)) {
                        actionValueWasDuplicated = true
                    }
                    actionValues.add(actionValue)
                }
            }

            // 컨넥터의 종류에 따라 validate 를 수행한다.
            when (connectorIsCondition + connectorIsAction) {
                WfElementConstants.ConnectorConditionValue.NONE.value -> return setFailedMessage("There is no condition-value or action-value of the connector.")
                WfElementConstants.ConnectorConditionValue.CONDITION.value -> {
                    // condition-value 중복을 확인한다.
                    if (conditionValueWasDuplicated) {
                        return setFailedMessage("Condition-value was duplicated.")
                    }
                }
                WfElementConstants.ConnectorConditionValue.ACTION.value -> {
                    // 게이트웨이는 condition-item 으로 #{action} 을 가져야한다.
                    if (conditionItem != WfElementConstants.AttributeValue.ACTION.value) {
                        return setFailedMessage("The condition-item of gateway should be action.")
                    }
                    // action-value 중복을 확인한다.
                    if (actionValueWasDuplicated) {
                        return setFailedMessage("Action-value was duplicated.")
                    }
                }
                WfElementConstants.ConnectorConditionValue.DUPLICATION.value -> return setFailedMessage("You only have to choose one of condition-value and action-value.")
            }
        } else {
            // gateway 의 arrowConnector 1개이면 condition-item 은 필수값이 아니므로
            // attributeRequired 값이 true 이더라도 필터링하여 제외시킨다
            val elementDataTo = element.elementDataEntities.toMutableList()
            val filteredElementDataEntities = elementDataTo.filter {
                it.attributeId != WfElementConstants.AttributeId.CONDITION_ITEM.value
            }.toMutableList()
            element.elementDataEntities.clear()
            element.elementDataEntities.addAll(filteredElementDataEntities)
        }
        return true
    }

    override fun failInfo(): String {
        return "Gateway simulation failed. $elementInformation"
    }
}
