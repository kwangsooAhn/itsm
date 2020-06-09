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

        // condition item 은 값이 있어야한다.
        val conditionItem = element.getElementDataValue(WfElementConstants.AttributeId.CONDITION_ITEM.value)
        val emptyCondition = conditionItem?.isBlank() ?: true
        if (emptyCondition) {
            return setFailedMessage("Condition item empty.")
        }

        // 게이트웨이의 분기조건들은 서로 중첩되지 않아야 한다.
        val conditionValues = mutableListOf<String?>()
        val arrowConnector = wfElementRepository.findAllArrowConnectorElement(element.elementId)
        arrowConnector.forEach {
            val conditionValue = it.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)
            if (conditionValues.contains(conditionValue)) {
                return setFailedMessage("Condition value duplicate.")
            }
            conditionValues.add(conditionValue)
        }

        // 게이트웨이에서 나가는 조건으로 map_id를 이용하는 경우 해당 map_id가 문서에 있는지 체크한다. ${}
        // TODO 발행전에는 document를 만들수가 없다. 내용 확인하고 추가.
//        val regexComponentMappingId = WfElementConstants.RegexCondition.MAPPINGID.value.toRegex()
//        if (conditionItem != null && conditionItem.matches(regexComponentMappingId)) {
//            val mappingId = conditionItem.trim().replace("\${", "").replace("}", "")
//
//        }
        // 게이트웨이는 입출력이 모두 1개 이상씩 존재 해야 하며, 입력이 1개거나 출력이 1개여야 한다.

        return true
    }

    override fun failInfo(): String {
        return "Gateway simulation failed. $elementInformation"
    }
}
