package co.brainz.workflow.element.service

import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.element.repository.ElementRepository
import co.brainz.workflow.token.dto.TokenSaveDto
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
    fun getNextElement(elementId: String, tokenSaveDto: TokenSaveDto): ElementEntity {
        // TODO 프로세스 디자이너에서 분기시 조건을 기술하는 문법을 정의하고 그 문법을 파싱해서 분기를 결정하도록 구현 필요.
        lateinit var selectedElement: ElementEntity
        elementRepository.findAllArrowConnectorElement(elementId).forEach { element ->
            selectedElement = elementRepository.findTargetElement(element.elementId)
            //element.getElementDataValue(ElementConstants.AttributeId.CONDITION.value)
        }
        return selectedElement
    }


}
