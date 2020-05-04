package co.brainz.workflow.engine.process.service.simulation.element.impl

import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.engine.process.service.simulation.element.WfProcessSimulationElement

/**
 * 시뮬레이션 검증 - subprocess
 */
class WfProcessSimulationSubProcess(
    wfElementRepository: WfElementRepository,
    wfProcessRepository: WfProcessRepository

) : WfProcessSimulationElement(wfElementRepository, wfProcessRepository) {
    override fun validate(element: WfElementEntity): Boolean {

        // 서브프로세스 엘리먼트에는 발행상태인 프로세스가 등록되어 있어야 한다.
        val processId = element.getElementDataValue(WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value) ?: ""
        val process = wfProcessRepository.findByProcessId(processId)
        if (process == null) {
            setFailedMessage("process does not exist.")
            return false
        }
        if (process.processStatus != WfProcessConstants.Status.PUBLISH.code) {
            setFailedMessage("process status has not published.")
            return false
        }
        return true
    }

    override fun failInfo(): String {
        return "Sub-process simulation failed. $elementInformation"
    }
}
