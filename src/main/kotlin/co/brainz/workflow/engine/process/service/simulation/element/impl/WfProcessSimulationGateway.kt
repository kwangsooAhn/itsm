package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - gateway
 */
class WfProcessSimulationGateway(private val wfElementRepository: WfElementRepository) : WfProcessSimulationElement() {
    override fun validate(element: WfElementEntity): Boolean {

        lateinit var elementData: List<WfElementDataEntity>

        val arrowConnector = wfElementRepository.findAllArrowConnectorElement(element.elementId)

        // gateway 의 arrowConnector 2개 이상이면 condition-item 은 값이 반드시 있어야 한다.
        elementData = if (arrowConnector.size > 1) {
            val conditionItem = element.getElementDataValue(WfElementConstants.AttributeId.CONDITION_ITEM.value)
            val emptyCondition = conditionItem?.isBlank() ?: true
            if (emptyCondition) {
                return setFailedMessage("Condition item empty.")
            }
            element.elementDataEntities
        } else {
            element.elementDataEntities.filter {
                it.attributeId != WfElementConstants.AttributeId.CONDITION_ITEM.value
            }
        }

        // 게이트웨이의 분기조건들은 서로 중첩되지 않아야 한다.
        val conditionValues = mutableListOf<String?>()
        arrowConnector.forEach {
            val conditionValue = it.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)
            if (conditionValues.contains(conditionValue)) {
                return setFailedMessage("Condition value duplicate.")
            }
            conditionValues.add(conditionValue)
        }

        return super.requiredValueVerification(elementData)
    }

    override fun failInfo(): String {
        return "Gateway simulation failed. $elementInformation"
    }
}
