package co.brainz.workflow.document.service

import co.brainz.workflow.document.dto.DocumentDto
import co.brainz.workflow.document.repository.DocumentRepository
import co.brainz.workflow.form.dto.FormComponentViewDto
import co.brainz.workflow.form.service.WFFormService
import org.springframework.stereotype.Service

@Service
class WFDocumentService(private val wfFormService: WFFormService,
                        private val documentRepository: DocumentRepository) {

    /**
     * Search Documents.
     *
     * @return List<DocumentDto>
     */
    fun documents(): List<DocumentDto> {

        val documents = mutableListOf<DocumentDto>()
        val documentEntities = documentRepository.findAll()
        for (document in documentEntities) {
            val documentDto = DocumentDto(
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
    fun document(documentId: String): FormComponentViewDto? {
        val documentEntity = documentRepository.findDocumentEntityByDocumentId(documentId)
        return wfFormService.form(documentEntity.form.formId)
    }
}
