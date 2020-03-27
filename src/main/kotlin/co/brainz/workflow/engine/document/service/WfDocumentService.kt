package co.brainz.workflow.engine.document.service

import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfDocumentService(private val wfFormService: WfFormService,
                        private val wfFormRepository: WfFormRepository,
                        private val wfDocumentRepository: WfDocumentRepository,
                        private val wfInstanceRepository: WfInstanceRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(WfFormMapper::class.java)

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
     * @return WfFormComponentViewDto?
     */
    fun document(documentId: String): WfFormComponentViewDto? {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val formEntity = wfFormRepository.findWfFormEntityByFormId(documentEntity.form.formId)
        val formViewDto = wfFormMapper.toFormViewDto(formEntity.get())
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        for (component in formEntity.get().components!!) {
            val attributes = wfFormService.makeAttributes(component)
            val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

            val map = LinkedHashMap<String, Any>()
            map["componentId"] = component.componentId
            map["attributes"] = attributes
            map["values"] = values
            components.add(map)
        }

        return WfFormComponentViewDto(
                form = formViewDto,
                components = components
        )
    }

    /**
     * Create Document.
     *
     * @param documentDto
     * @return WfDocumentDto
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
            formId = dataEntity.form.formId,
            procId = dataEntity.process.processId,
            createDt = dataEntity.createDt,
            createUserKey = dataEntity.createUserKey
        )
    }

    /**
     * 다큐먼트를 삭제한다.
     *
     * @param documentId 다큐먼트 아이디
     */
    fun deleteDocument(documentId: String): Boolean {

        // 인스턴스에서 해당 다큐먼트가 있는지 체크.
        val selectedDocument = wfDocumentRepository.getOne(documentId)
        val instanceCnt = wfInstanceRepository.countByDocument(selectedDocument)

        // 있으면 삭제
        val isDel = if (instanceCnt == 0) {
            logger.debug("Try delete document...")
            wfDocumentRepository.deleteByDocumentId(documentId)
            true
        } else {
            false
        }
        logger.info("Delete document result. {}", isDel)
        return isDel
    }
}
