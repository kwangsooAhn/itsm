package co.brainz.workflow.engine.document.service

import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.service.WfFormService
import org.springframework.stereotype.Service

@Service
class WfDocumentService(private val wfFormService: WfFormService,
                        private val wfDocumentRepository: WfDocumentRepository) {

    /**
     * Search Documents.
     *
     * @return List<DocumentDto>
     */
    fun documents(): List<WfDocumentDto> {

        val documents = mutableListOf<WfDocumentDto>()
        val documentEntities = wfDocumentRepository.findAll()
        for (document in documentEntities) {
            val documentDto = WfDocumentDto(
                    documentId = document.documentId,
                    documentName = document.documentName,
                    documentDesc = document.documentDesc,
                    procId = document.process.processId,
                    formId = document.form.formId,
                    createDt = document.createDt,
                    createUserKey = document.createUserKey,
                    updateDt = document.updateDt,
                    updateUserKey = document.updateUserKey
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
    fun document(documentId: String): WfFormComponentViewDto? {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        return wfFormService.form(documentEntity.form.formId)
    }
}
