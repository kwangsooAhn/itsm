package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - arrow connector
 */
class WfProcessSimulationArrow(wfElementRepository: WfElementRepository, wfProcessRepository: WfProcessRepository) :
    WfProcessSimulationElement(wfElementRepository, wfProcessRepository) {

    override fun validate(element: WfElementEntity): Boolean {
        // sourceElement가 gatewary일 때는 connector의 condition 값이 있어야 한다.
        val sourceElementId = element.getElementDataValue(WfElementConstants.AttributeId.SOURCE_ID.value)
        val emptyCondition =
            element.getElementDataValue(WfElementConstants.AttributeId.CONDITION_VALUE.value)?.isBlank() ?: true
        val sourceElement = wfElementRepository.getOne(sourceElementId!!)
        if ((sourceElement.elementType == WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value
                    || sourceElement.elementType == WfElementConstants.ElementType.PARALLEL_GATEWAY.value
                    || sourceElement.elementType == WfElementConstants.ElementType.INCLUSIVE_GATEWAY.value)
            && emptyCondition
        ) {
            setFailedMessage("connector is empty.")
            return false
        }

        // action name과 action value는 쌍으로 있어야 하며 2개중 하나의 값만 있으면 안된다.
        val isName = element.getElementDataValue(WfElementConstants.AttributeId.ACTION_NAME.value)?.isBlank() ?: true
        val isValue = element.getElementDataValue(WfElementConstants.AttributeId.ACTION_VALUE.value)?.isBlank() ?: true
        if ((isName && !isValue) || (!isName && isValue)) {
            setFailedMessage("Check the action name and value .")
            return false
        }

        return true
    }

    override fun failInfo(): String {
        return "Arrow connector simulation failed. $elementInformation"
    }
}
