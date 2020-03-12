package co.brainz.workflow.element.service

import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.element.repository.ElementRepository
import co.brainz.workflow.token.dto.TokenDto
import org.springframework.stereotype.Service

@Service
class WFElementService(private val elementRepository: ElementRepository) {

    /**
     * get element by ProcessId, ElementType
     *
     * @param processId, elementType
     * @return ElementEntity
     */
    fun getElementId (processId : String, elementType : String) : ElementEntity {
        return elementRepository.findByProcessIdAndElementType(processId, elementType)
    }

    /**
     * get next element in process.
     *
     * @param elementId
     * @return ElementEntity
     */
    fun getNextElement(elementId: String, tokenDto: TokenDto): ElementEntity {
        lateinit var selectedElement: ElementEntity
        elementRepository.findAllArrowConnectorElement(elementId).forEach { element ->
            selectedElement = elementRepository.findTargetElement(element.elementId)
            //element.getElementDataValue(ElementConstants.AttributeId.CONDITION.value)
        }
        return selectedElement
    }


}
