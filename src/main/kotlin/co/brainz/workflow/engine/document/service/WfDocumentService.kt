package co.brainz.workflow.engine.document.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.document.constants.WfDocumentConstants
import co.brainz.workflow.engine.document.dto.WfDocumentDisplaySaveDto
import co.brainz.workflow.engine.document.dto.WfDocumentDisplayViewDto
import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentDataEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentDataRepository
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.repository.WfElementDataRepository
import co.brainz.workflow.engine.element.repository.WfElementRepository
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
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import org.mapstruct.factory.Mappers
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
    private val wfElementDataRepository: WfElementDataRepository,
    private val aliceNumberingRuleRepository: AliceNumberingRuleRepository
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
                processId = document.process.processId,
                formId = document.form.formId,
                createDt = document.createDt,
                createUserKey = document.createUserKey,
                updateDt = document.updateDt,
                updateUserKey = document.updateUserKey,
                documentNumberingRuleId = document.numberingRule.numberingId
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
            processId = document.process.processId,
            formId = document.form.formId,
            createDt = document.createDt,
            createUserKey = document.createUserKey,
            updateDt = document.updateDt,
            updateUserKey = document.updateUserKey,
            documentNumberingRuleId = document.numberingRule.numberingId
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
            //TODO: 추후 동적으로 변경할 수 있도록 구현해야 함.
            map["displayType"] = WfDocumentConstants.DisplayType.EDITABLE.value
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
        val processId = documentDto.processId
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
            documentStatus = documentDto.documentStatus,
            numberingRule = aliceNumberingRuleRepository.findById(documentDto.documentNumberingRuleId).get()
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)

        // 신청서 양식 정보 초기화
        createDocumentDisplay(dataEntity)

        return WfDocumentDto(
            documentId = dataEntity.documentId,
            documentName = dataEntity.documentName,
            documentDesc = dataEntity.documentDesc,
            formId = dataEntity.form.formId,
            processId = dataEntity.process.processId,
            createDt = dataEntity.createDt,
            createUserKey = dataEntity.createUserKey,
            documentNumberingRuleId = dataEntity.numberingRule.numberingId
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
        val process = WfProcessEntity(processId = documentDto.processId)
        wfDocumentEntity.documentName = documentDto.documentName
        wfDocumentEntity.documentDesc = documentDto.documentDesc
        wfDocumentEntity.documentStatus = documentDto.documentStatus
        wfDocumentEntity.updateUserKey = documentDto.updateUserKey
        wfDocumentEntity.updateDt = documentDto.updateDt
        wfDocumentEntity.form = form
        wfDocumentEntity.process = process
        wfDocumentEntity.numberingRule = aliceNumberingRuleRepository.findById(documentDto.documentNumberingRuleId).get()
        wfDocumentRepository.save(wfDocumentEntity)

        when (documentDto.documentStatus) {
            WfDocumentConstants.Status.USE.code -> {
                val wfFormEntity = wfFormRepository.findWfFormEntityByFormId(documentDto.formId).get()
                if (wfFormEntity.formStatus != WfFormConstants.FormStatus.USE.value) {
                    wfFormEntity.formStatus = WfFormConstants.FormStatus.USE.value
                    wfFormRepository.save(wfFormEntity)
                }
                val wfProcessEntity = wfProcessRepository.findByProcessId(documentDto.processId)
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
            wfDocumentDataRepository.deleteByDocumentId(documentId)
            wfDocumentRepository.deleteByDocumentId(documentId)
            true
        } else {
            false
        }
        logger.info("Delete document result. {}", isDel)
        return isDel
    }

    /**
     * Create Document Display.
     *
     * @param documentEntity
     */
    fun createDocumentDisplay(documentEntity: WfDocumentEntity) {
        val wfDocumentDataEntities: MutableList<WfDocumentDataEntity> = mutableListOf()
        val componentEntities = wfComponentRepository.findByFormId(documentEntity.form.formId)
        val elementEntities = wfElementRepository.findUserTaskByProcessId(documentEntity.process.processId)
        for (component in componentEntities) {
            for (element in elementEntities) {
                val documentDataEntity = WfDocumentDataEntity(
                    documentId = documentEntity.documentId,
                    componentId = component.componentId,
                    elementId = element.elementId
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
     * @param documentId
     * @return WfDocumentDisplayViewDto
     */
    fun getDocumentDisplay(documentId: String): WfDocumentDisplayViewDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val elementEntities = wfElementDataRepository.findElementDataByProcessId(documentEntity.process.processId)
        val componentEntities = wfComponentRepository.findByFormId(documentEntity.form.formId)
        val displayList = wfDocumentDataRepository.findByDocumentId(documentId)

        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (component in componentEntities) {
            val displayValue: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            for (display in displayList) {
                if (display.componentId == component.componentId) {
                    val displayMap = LinkedHashMap<String, Any>()
                    displayMap["elementId"] = display.elementId
                    displayMap["display"] = display.display
                    displayValue.add(displayMap)
                }
            }
            val componentMap = LinkedHashMap<String, Any>()
            val componentData = wfComponentDataRepository.findComponentDataByComponentId(component.componentId, "label")
            val attributeValue = if (componentData.size > 0) {
                // 화면에 표시하기 위한 컴포넌트의 이름속성만 분리
                componentData[0].attributeValue.split("\"text\":\"")[1].split("\"}")[0]
            } else {
                // 컴포넌트 라벨 속성이 없는 경우, 컴포넌트 타입을 화면에 표시한다.
                component.componentType
            }
            componentMap["componentId"] = component.componentId
            componentMap["attributeValue"] = attributeValue
            componentMap["displayValue"] = displayValue
            components.add(componentMap)
        }
        return WfDocumentDisplayViewDto(
            documentId = documentId,
            elements = elementEntities,
            components = components
        )
    }

    /**
     * Update Document Display data.
     *
     * @param wfDocumentDisplaySaveDto
     * @return Boolean
     */
    fun updateDocumentDisplay(wfDocumentDisplaySaveDto: WfDocumentDisplaySaveDto): Boolean {
        val documentId = wfDocumentDisplaySaveDto.documentId
        wfDocumentDataRepository.deleteByDocumentId(documentId)
        val displays = wfDocumentDisplaySaveDto.displays
        displays.forEach {
            wfDocumentDataRepository.save(
                WfDocumentDataEntity(
                    documentId = documentId,
                    componentId = it.getValue("componentId").toString(),
                    elementId = it.getValue("elementId").toString(),
                    display = it.getValue("display").toString()
                )
            )
        }
        return true
    }
}
