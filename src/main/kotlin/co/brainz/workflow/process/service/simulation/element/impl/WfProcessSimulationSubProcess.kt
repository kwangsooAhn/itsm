package co.brainz.workflow.process.service.simulation.element.impl

import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - subprocess
 */
class WfProcessSimulationSubProcess(private val wfDocumentRepository: WfDocumentRepository) :
    WfProcessSimulationElement() {

    override fun validate(element: WfElementEntity): Boolean {

        // 서브프로세스 엘리먼트에는 발행상태인 프로세스가 등록되어 있어야 한다.
        val documentId = element.getElementDataValue(WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value) ?: ""
        val document = wfDocumentRepository.findByDocumentId(documentId)
            ?: return setFailedMessage("Document does not exist.")

        if (!super.checkProcessStatus(document.process.processStatus)) {
            return setFailedMessage("process status has not published.")
        }
        return true
    }

    override fun failInfo(): String {
        return "Sub-process simulation failed. $elementInformation"
    }
}
