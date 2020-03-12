package co.brainz.workflow.element.service

import co.brainz.workflow.element.constants.ElementConstants
import co.brainz.workflow.element.entity.ElementEntity
import co.brainz.workflow.element.repository.ElementRepository
import co.brainz.workflow.token.dto.TokenSaveDto
import org.springframework.stereotype.Service

@Service
class WFElementService(private val elementRepository: ElementRepository) {

    /**
     * get element by ProcessId, ElementType
     *
     * @param processId
     * @return ElementEntity
     */
    fun getStartElement (processId : String) : ElementEntity {
        // TODO
        // start element는 몇 종류가 있는데 1개의 프로세스에 commonStart는 1개만 존재하지만 다른 종류의 start는 복수개가 존재가능.
        // 이에 대한 처리를 위해 getStartElement에 파라미터가 추가될 필요가 있다.
        return elementRepository.findByProcessIdAndElementType(processId, ElementConstants.ElementType.COMMON_START_EVENT.value)
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
        elementRepository.findAllArrowConnectorElement(elementId).forEach { arrow ->
            selectedElement = elementRepository.findTargetElement(arrow.elementId)
            //element.getElementDataValue(ElementConstants.AttributeId.CONDITION.value)
        }
        return selectedElement
    }
}
