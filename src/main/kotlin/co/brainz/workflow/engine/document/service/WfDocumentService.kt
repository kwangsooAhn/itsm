package co.brainz.workflow.engine.document.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentDataEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentDataRepository
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.repository.WfElementDataRepository
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
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
        private val wfDocumentDataRepository: WfDocumentDataRepository,
        private val wfInstanceRepository: WfInstanceRepository,
        private val wfProcessRepository: WfProcessRepository,
        private val wfFormRepository: WfFormRepository,
        private val wfComponentRepository: WfComponentRepository,
        private val wfComponentDataRepository: WfComponentDataRepository,
        private val wfElementRepository: WfElementRepository,
        private val wfElementDataRepository: WfElementDataRepository
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
                components = components,
                actions = wfActionService.actionInit(documentEntity.process.processId)
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
                createUserKey = documentDto.createUserKey
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)
        
        // 신청서 양식 정보 초기화
        createDocumentData(dataEntity)

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
            // 신청서 양식 정보 삭제
            wfDocumentDataRepository.deleteById(documentId)
            // 신청서 정보 삭제
            wfDocumentRepository.deleteByDocumentId(documentId)
            true
        } else {
            false
        }
        logger.info("Delete document result. {}", isDel)
        return isDel
    }

    /**
     * Create Document Data.
     *
     * @param documentDto
     * @return WfDocumentDto
     */
    fun createDocumentData(documentDto: WfDocumentEntity) {
        val wfDocumentDataEntities: MutableList<WfDocumentDataEntity> = mutableListOf()
        val componentEntities = wfComponentRepository.findByFormId(documentDto.form.formId)
        val componentIds: MutableList<String> = mutableListOf()
        for (component in componentEntities) {
            componentIds.add(component.componentId)
        }
        val elementEntities = wfElementRepository.findUserTaskByProcessId(documentDto.process.processId)
        val elementIds: MutableList<String> = mutableListOf()
        for (element in elementEntities) {
            elementIds.add(element.elementId)
        }
        for (componentId in componentIds) {
            for (elementId in elementIds) {
                val documentDataEntity = WfDocumentDataEntity(
                        documentId = documentDto.documentId,
                        componentId = componentId,
                        elementId = elementId
                )
                wfDocumentDataEntities.add(documentDataEntity)
            }
        }

        if (wfDocumentDataEntities.isNotEmpty()) {
            logger.debug("documentData setting...")
            wfDocumentDataRepository.saveAll(wfDocumentDataEntities)
        }
    }

    /**
     * Search Document Display data.
     *
     * @return List<DocumentDataDto>
     */
    fun documentDisplay(documentId: String): String {// WfDocumentDisplayViewDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val componentList = wfComponentDataRepository .findComponentDataByFormId(documentEntity.form.formId, "label")
        val elementList = wfElementDataRepository.findElementDataByProcessId(documentEntity.process.processId, "userTask", "name")

        // 인스턴스에서 해당 다큐먼트가 있는지 체크.
//        val selectedDocument = wfDocumentRepository.getOne(documentId)
//        val instanceCnt = wfInstanceRepository.countByDocument(selectedDocument)

//        val documents = mutableListOf<WfDocumentDisplayDataDto>()
//        val documentEntities = wfDocumentDataRepository.findByDocumentId(documentId)
//        for (document in documentEntities) {
//            val documentDataDto = WfDocumentDisplayDataDto(
//                    documentId = document.documentId,
//                    componentId = document.componentId,
//                    elementId = document.elementId,
//                    display = document.display
//            )
//            documents.add(documentDataDto)
//        }

        return "documents"
    }

}
