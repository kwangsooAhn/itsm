package co.brainz.workflow.engine.element.service

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.token.dto.WfTokenDto
import org.springframework.stereotype.Service

@Service
class WfElementService(private val wfElementRepository: WfElementRepository) {

    /**
     * get element by ProcessId, ElementType
     *
     * @param processId
     * @return ElementEntity
     */
    fun getStartElement (processId : String) : WfElementEntity {
        // TODO
        // start element는 몇 종류가 있는데 1개의 프로세스에 commonStart는 1개만 존재하지만 다른 종류의 start는 복수개가 존재가능.
        // 이에 대한 처리를 위해 getStartElement에 파라미터가 추가될 필요가 있다.
        return wfElementRepository.findByProcessIdAndElementType(processId, WfElementConstants.ElementType.COMMON_START_EVENT.value)
    }

    /**
     * get next element in process.
     *
     * @param elementId
     * @return ElementEntity
     */
    fun getNextElement(elementId: String, wfTokenDto: WfTokenDto): WfElementEntity {
        lateinit var selectedElement: WfElementEntity
        wfElementRepository.findAllArrowConnectorElement(elementId).forEach { arrow ->
            selectedElement = wfElementRepository.findTargetElement(arrow.elementId)
            //element.getElementDataValue(ElementConstants.AttributeId.CONDITION.value)
        }
        return selectedElement
    }
}
