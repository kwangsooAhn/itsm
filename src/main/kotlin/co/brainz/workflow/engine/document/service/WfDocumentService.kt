package co.brainz.workflow.engine.document.service

import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.provider.constants.RestTemplateConstants
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


    /**
     * Create Document.
     *
     * @param documentDto
     * @return DocumentDto
     */
    fun createDocument(documentDto: WfDocumentDto): WfDocumentDto {
        val form = WfFormEntity(formId = documentDto.formId)
        val process = WfProcessEntity(processId = documentDto.procId)
        val documentEntity = WfDocumentEntity(
                documentId = documentDto.documentId,
                documentName = documentDto.documentName,
                documentDesc = documentDto.documentDesc,
                form = form,
                process = process,
                createDt = documentDto.createDt,
                createUserKey = documentDto.createUserKey
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)

        return WfDocumentDto(
                documentId = dataEntity.documentId,
                documentName = dataEntity.documentName,
                documentDesc = dataEntity.documentDesc,
                // 상단 이슈 해결되고 나서 적용할 것
                formId = "",
                procId = "",
                createDt = dataEntity.createDt,
                createUserKey = dataEntity.createUserKey
        )
    }

    /**
     * Delete Document.
     *
     * @param documentId
     */
    fun deleteDocument(documentId: String) {
//        val documentEntity = wfDocumentRepository.findByDocumentId(documentId)
//        if (documentEntity.documentStatus == RestTemplateConstants.DocumentStatus.PUBLISH.value
//                || documentEntity.processStatus == WfProcessConstants.Status.DESTROY.code
//        ) {
//            return false
//        } else {
            wfDocumentRepository.removeWfDocumentEntityByDocumentId(documentId)
//        }
//        return true
    }
}
