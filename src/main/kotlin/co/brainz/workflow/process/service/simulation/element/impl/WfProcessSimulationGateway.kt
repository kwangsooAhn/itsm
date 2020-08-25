/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
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
                return failed("Condition item empty.")
            }

            // 컨넥터들의 종류가 condition인지 action인지 확인한다.
            var connectorIsCondition = WfElementConstants.ConnectorConditionValue.NONE.value
            var connectorIsAction = WfElementConstants.ConnectorConditionValue.NONE.value
            arrowConnectors.forEach {
                if (it.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)
                        ?.isNotBlank() == true
                ) {
                    connectorIsCondition = WfElementConstants.ConnectorConditionValue.CONDITION.value
                }
                if (it.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)?.isNotBlank() == true) {
                    connectorIsAction = WfElementConstants.ConnectorConditionValue.ACTION.value
                }
            }

            // 컨넥터의 종류에 따라 validate 를 수행한다.
            when (connectorIsCondition + connectorIsAction) {
                WfElementConstants.ConnectorConditionValue.NONE.value ->
                    return failed("There is no value of the connector.")
                WfElementConstants.ConnectorConditionValue.CONDITION.value -> {
                    // connector 속성이 condition 이면 condition-item 으로 #{action} 은 사용하지 않는다.
                    if (conditionItem == WfElementConstants.AttributeValue.ACTION.value) {
                        return failed("The condition-item of gateway should not be action.")
                    }
                }
                WfElementConstants.ConnectorConditionValue.ACTION.value -> {
                    // connector 속성이 action이면 condition-item 으로 #{action} 을 가져야한다.
                    if (conditionItem != WfElementConstants.AttributeValue.ACTION.value) {
                        return failed("The condition-item of gateway should be action.")
                    }
                }
                WfElementConstants.ConnectorConditionValue.DUPLICATION.value ->
                    return failed("Connector of gateway only have to choose one of condition and action.")
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

    override fun failedMessage(): String {
        return "Gateway simulation failed. $simulationFailedMsg"
    }
}
