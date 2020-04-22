package co.brainz.workflow.engine.document.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.document.constants.WfDocumentConstants
import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import org.mapstruct.factory.Mappers
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfDocumentService(
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfProcessRepository: WfProcessRepository,
    private val wfFormRepository: WfFormRepository
) {

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
                    documentStatus = document.documentStatus,
                    procId = document.process!!.processId,
                    formId = document.form!!.formId,
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
     * @return WfDocumentDto
     */
    fun getDocument(documentId: String): WfDocumentDto {
        val document = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        return WfDocumentDto(
                documentId = document.documentId,
                documentName = document.documentName,
                documentDesc = document.documentDesc,
                documentStatus = document.documentStatus,
                procId = document.process!!.processId,
                formId = document.form!!.formId,
                createDt = document.createDt,
                createUserKey = document.createUserKey,
                updateDt = document.updateDt,
                updateUserKey = document.updateUserKey
        )
    }

    /**
     * Search Document Data.
     *
     * @param documentId
     * @return WfFormComponentViewDto?
     */
    fun getDocumentData(documentId: String): WfFormComponentViewDto? {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val formEntity = wfFormRepository.findWfFormEntityByFormId(documentEntity.form!!.formId)
        val formViewDto = wfFormMapper.toFormViewDto(formEntity.get())
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (component in formEntity.get().components!!) {
            val attributes = wfFormService.makeAttributes(component)
            val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

            val map = LinkedHashMap<String, Any>()
            map["componentId"] = component.componentId
            map["attributes"] = attributes
            map["values"] = values
            //TODO: 실 데이터로 변경.
            map["displayType"] = when (attributes["type"]) {
                "text", "textarea", "select", "radio", "checkbox", "label", "image", "divider", "date", "time", "datetime", "fileupload", "custom-code" -> "editable"
                else -> "readonly" //readonly, editable, editable_required, hidden
            }
            components.add(map)
        }

        return WfFormComponentViewDto(
                form = formViewDto,
                components = components,
                actions = wfActionService.actionInit(documentEntity.process!!.processId)
        )
    }

    /**
     * Create Document.
     *
     * @param documentDto
     * @return WfDocumentDto
     */
    fun createDocument(documentDto: WfDocumentDto): WfDocumentDto {
        val formId = documentDto.formId
        val processId = documentDto.procId
        val selectedForm = wfFormRepository.getOne(formId)
        val selectedProcess = wfProcessRepository.getOne(processId)
        val selectedDocument = wfDocumentRepository.findByFormAndProcess(selectedForm, selectedProcess)
        if (selectedDocument != null) {
            throw AliceException(AliceErrorConstants.ERR, "Duplication document. check form and process")
        }

        val form = WfFormEntity(formId = formId)
        val process = WfProcessEntity(processId = processId)
        val documentEntity = WfDocumentEntity(
                documentId = documentDto.documentId,
                documentName = documentDto.documentName,
                documentDesc = documentDto.documentDesc,
                form = form,
                process = process,
                createDt = documentDto.createDt,
                createUserKey = documentDto.createUserKey,
                documentStatus = documentDto.documentStatus
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)

        return WfDocumentDto(
                documentId = dataEntity.documentId,
                documentName = dataEntity.documentName,
                documentDesc = dataEntity.documentDesc,
                documentStatus = dataEntity.documentStatus,
                formId = dataEntity.form!!.formId,
                procId = dataEntity.process!!.processId,
                createDt = dataEntity.createDt,
                createUserKey = dataEntity.createUserKey
        )
    }

    /**
     * Update Document.
     *
     * @param documentDto
     * @return Boolean
     */
    fun updateDocument(documentDto: WfDocumentDto): Boolean {

        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentDto.documentId)
        val form = WfFormEntity(formId = documentDto.formId)
        val process = WfProcessEntity(processId = documentDto.procId)
        wfDocumentEntity.documentName = documentDto.documentName
        wfDocumentEntity.documentDesc = documentDto.documentDesc
        wfDocumentEntity.documentStatus = documentDto.documentStatus
        wfDocumentEntity.updateUserKey = documentDto.updateUserKey
        wfDocumentEntity.updateDt = documentDto.updateDt
        wfDocumentEntity.form = form
        wfDocumentEntity.process = process
        wfDocumentRepository.save(wfDocumentEntity)

        when (documentDto.documentStatus) {
            WfDocumentConstants.Status.USE.code -> {
                val wfFormEntity = wfFormRepository.findWfFormEntityByFormId(documentDto.formId).get()
                if (wfFormEntity.formStatus != WfFormConstants.FormStatus.USE.value) {
                    wfFormEntity.formStatus = WfFormConstants.FormStatus.USE.value
                    wfFormRepository.save(wfFormEntity)
                }
                val wfProcessEntity = wfProcessRepository.findByProcessId(documentDto.procId)
                if (wfProcessEntity.processStatus != WfProcessConstants.Status.USE.code) {
                    wfProcessEntity.processStatus = WfProcessConstants.Status.USE.code
                    wfProcessRepository.save(wfProcessEntity)
                }
            }
        }

        return true
    }

    /**
     * Delete Document.
     *
     * @param documentId
     * @return Boolean
     */
    fun deleteDocument(documentId: String): Boolean {
        val selectedDocument = wfDocumentRepository.getOne(documentId)
        val instanceCnt = wfInstanceRepository.countByDocument(selectedDocument)

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
