package co.brainz.workflow.document.service

import co.brainz.workflow.document.dto.DocumentDto
import co.brainz.workflow.form.dto.FormComponentViewDto
import co.brainz.workflow.form.dto.FormViewDto
import co.brainz.workflow.form.service.WFFormService
import co.brainz.workflow.process.constants.ProcessConstants
import co.brainz.workflow.process.repository.ProcessMstRepository
import co.brainz.workflow.process.service.WFProcessService
import org.springframework.stereotype.Service

@Service
class WFDocumentService(private val wfFormService: WFFormService,
                        private val wfProcessService: WFProcessService,
                        private val processMstRepository: ProcessMstRepository) {

    /**
     * Search Documents.
     *
     * @return List<DocumentDto>
     */
    fun documents(): List<DocumentDto> {

        val documents = mutableListOf<DocumentDto>()
        val processMstEntities = processMstRepository.findProcessMstEntityByProcStatus(ProcessConstants.Status.PUBLISH.code)
        for (processMstEntity in processMstEntities) {
            val documentDto = DocumentDto(
                    documentId = processMstEntity.procId,
                    documentName = processMstEntity.procName,
                    documentDesc = processMstEntity.procDesc
            )
            documents.add(documentDto)
        }

        return documents
    }

    /**
     * Search Document.
     *
     * @param documentId
     * @return FormComponentViewDto
     */
    fun document(documentId: String): FormComponentViewDto? {
        val processDto = wfProcessService.getProcess(documentId)
        return if (processDto.processStatus == ProcessConstants.Status.PUBLISH.code) {
            processDto.formId?.let { wfFormService.form(it) }
        } else {
            null
        }
    }
}
