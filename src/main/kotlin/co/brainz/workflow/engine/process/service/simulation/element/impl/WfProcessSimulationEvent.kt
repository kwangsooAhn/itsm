package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - event (start, end)
 *
 * start 또는 end 이벤트는 1개만 존재해야한다.
 */
class WfProcessSimulationEvent(private val wfDocumentRepository: WfDocumentRepository) : WfProcessSimulationElement() {

    override fun validate(element: WfElementEntity): Boolean {
        val process = element.processEntity

        // 시그널 이벤트
        if (element.elementType == WfElementConstants.ElementType.SIGNAL_SEND.value) {
            // 시그널 대상 다큐먼트들의 프로세스를 확인. 엘리먼트에는 발행상태인 프로세스가 등록되어 있어야 한다.
            val documentId =
                element.getElementDataValue(WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) ?: ""

            val document = wfDocumentRepository.findByDocumentId(documentId)
                ?: return setFailedMessage("Document does not exist. check signal target.")

            if (document.process.processStatus != WfProcessConstants.Status.PUBLISH.code) {
                return setFailedMessage("Process status has not published.")
            }

            if (document.form.formStatus != WfFormConstants.FormStatus.PUBLISH.value) {
                return setFailedMessage("Form status has not published.")
            }
        } else {
            // 시작 또는 종료 이벤트
            val findElements =
                process?.elementEntities?.filter {
                    it.elementType == element.elementType
                }

            if (!(findElements?.size == 1)) {
                return setFailedMessage("There should be only one start/end event.")
            }
        }

        return true
    }

    override fun failInfo(): String {
        return "Event simulation failed. $elementInformation"
    }
}
