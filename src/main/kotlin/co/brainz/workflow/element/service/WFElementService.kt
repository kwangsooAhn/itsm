package co.brainz.workflow.element.service

import co.brainz.workflow.element.constants.ElementConstants
import co.brainz.workflow.element.entity.ElementMstEntity
import co.brainz.workflow.element.repository.ElementMstRepository
import co.brainz.workflow.token.dto.TokenSaveDto
import org.springframework.stereotype.Service

@Service
class WFElementService(private val elementMstRepository: ElementMstRepository) {
    /**
     * get next element in process.
     *
     * @param elementId
     * @return ElementMstEntity
     */
    fun getNextElement(elementId: String, tokenSaveDto: TokenSaveDto): ElementMstEntity {
        // TODO 프로세스 디자이너에서 분기시 조건을 기술하는 문법을 정의하고 그 문법을 파싱해서 분기를 결정하도록 구현 필요.
        lateinit var selectedElement: ElementMstEntity
        elementMstRepository.findAllArrowConnectorElement(elementId).forEach { element ->
            selectedElement = elementMstRepository.findTargetElement(element.elementId)
            //element.getElementDataValue(ElementConstants.AttributeId.CONDITION.value)
        }
        return selectedElement;
    }


}
