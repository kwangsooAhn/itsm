package co.brainz.workflow.engine.document.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.engine.component.repository.WfComponentDataRepository
import co.brainz.workflow.engine.component.repository.WfComponentRepository
import co.brainz.workflow.engine.document.constants.WfDocumentConstants
import co.brainz.workflow.engine.document.entity.WfDocumentDataEntity
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentDataRepository
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.repository.WfElementDataRepository
import co.brainz.workflow.engine.element.repository.WfElementRepository
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.form.constants.WfFormConstants
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.form.service.WfFormService
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.process.constants.WfProcessConstants
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import co.brainz.workflow.engine.process.repository.WfProcessRepository
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateFormComponentViewDto
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
    private val aliceNumberingRuleRepository: AliceNumberingRuleRepository,
    private val wfElementService: WfElementService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(WfFormMapper::class.java)

    /**
     * Search Documents.
     *
     * @return List<RestTemplateDocumentDto>
     */
    fun documents(searchListDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto> {
        return wfDocumentRepository.findByDocuments(searchListDto)
    }

    /**
     * Search Document.
     *
     * @param documentId
     * @return RestTemplateDocumentDto
     */
    fun getDocument(documentId: String): RestTemplateDocumentDto {
        val document = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        return RestTemplateDocumentDto(
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
            documentNumberingRuleId = document.numberingRule.numberingId,
            documentColor = document.documentColor
        )
    }

    /**
     * Search Document Data.
     *
     * @param documentId
     * @return RestTemplateFormComponentViewDto?
     */
    fun getDocumentData(documentId: String): RestTemplateFormComponentViewDto? {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val formEntity = wfFormRepository.findWfFormEntityByFormId(documentEntity.form.formId)
        val formViewDto = wfFormMapper.toFormViewDto(formEntity.get())
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

        //init display: get first UserTask display info (commonStart -> UserTask).
        var documentDataEntities: List<WfDocumentDataEntity> = mutableListOf()
        val startElement = wfElementService.getStartElement(documentEntity.process.processId)
        when (startElement.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                val startArrow = wfActionService.getArrowElements(startElement.elementId)[0]
                val startUserTaskElementId = wfActionService.getNextElementId(startArrow)
                when (wfActionService.getElement(startUserTaskElementId).elementType) {
                    WfElementConstants.ElementType.USER_TASK.value -> {
                        documentDataEntities =
                            wfDocumentDataRepository.findByDocumentIdAndElementId(documentId, startUserTaskElementId)
                    }
                }
            }
        }

        for (component in formEntity.get().components!!) {
            val attributes = wfFormService.makeAttributes(component)
            val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

            val map = LinkedHashMap<String, Any>()
            map["componentId"] = component.componentId
            map["attributes"] = attributes
            map["values"] = values
            var displayType = WfDocumentConstants.DisplayType.EDITABLE.value
            for (documentDataEntity in documentDataEntities) {
                if (documentDataEntity.componentId == component.componentId) {
                    displayType = documentDataEntity.display
                }
            }
            map["displayType"] = displayType
            components.add(map)
        }

        return RestTemplateFormComponentViewDto(
            form = formViewDto,
            components = components,
            actions = wfActionService.actionInit(documentEntity.process.processId)
        )
    }

    /**
     * Create Document.
     *
     * @param restTemplateDocumentDto
     * @return RestTemplateDocumentDto
     */
    fun createDocument(restTemplateDocumentDto: RestTemplateDocumentDto): RestTemplateDocumentDto {
        val formId = restTemplateDocumentDto.formId
        val processId = restTemplateDocumentDto.processId
        val selectedForm = wfFormRepository.getOne(formId)
        val selectedProcess = wfProcessRepository.getOne(processId)
        val selectedDocument = wfDocumentRepository.findByFormAndProcess(selectedForm, selectedProcess)
        if (selectedDocument != null) {
            throw AliceException(AliceErrorConstants.ERR, "Duplication document. check form and process")
        }

        val form = WfFormEntity(formId = formId)
        val process = WfProcessEntity(processId = processId)
        val documentEntity = WfDocumentEntity(
            documentId = restTemplateDocumentDto.documentId,
            documentName = restTemplateDocumentDto.documentName,
            documentDesc = restTemplateDocumentDto.documentDesc,
            form = form,
            process = process,
            createDt = restTemplateDocumentDto.createDt,
            createUserKey = restTemplateDocumentDto.createUserKey,
            documentStatus = restTemplateDocumentDto.documentStatus,
            numberingRule = aliceNumberingRuleRepository.findById(restTemplateDocumentDto.documentNumberingRuleId).get(),
            documentColor = restTemplateDocumentDto.documentColor
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)

        // 신청서 양식 정보 초기화
        createDocumentDisplay(dataEntity)

        return RestTemplateDocumentDto(
            documentId = dataEntity.documentId,
            documentName = dataEntity.documentName,
            documentDesc = dataEntity.documentDesc,
            formId = dataEntity.form.formId,
            processId = dataEntity.process.processId,
            createDt = dataEntity.createDt,
            createUserKey = dataEntity.createUserKey,
            documentNumberingRuleId = dataEntity.numberingRule.numberingId,
            documentColor = dataEntity.documentColor
        )
    }

    /**
     * Update Document.
     *
     * @param restTemplateDocumentDto
     * @return Boolean
     */
    fun updateDocument(restTemplateDocumentDto: RestTemplateDocumentDto): Boolean {

        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(restTemplateDocumentDto.documentId)
        val form = WfFormEntity(formId = restTemplateDocumentDto.formId)
        val process = WfProcessEntity(processId = restTemplateDocumentDto.processId)
        wfDocumentEntity.documentName = restTemplateDocumentDto.documentName
        wfDocumentEntity.documentDesc = restTemplateDocumentDto.documentDesc
        wfDocumentEntity.documentStatus = restTemplateDocumentDto.documentStatus
        wfDocumentEntity.updateUserKey = restTemplateDocumentDto.updateUserKey
        wfDocumentEntity.updateDt = restTemplateDocumentDto.updateDt
        wfDocumentEntity.form = form
        wfDocumentEntity.process = process
        wfDocumentEntity.numberingRule =
            aliceNumberingRuleRepository.findById(restTemplateDocumentDto.documentNumberingRuleId).get()
        wfDocumentEntity.documentColor = restTemplateDocumentDto.documentColor
        wfDocumentRepository.save(wfDocumentEntity)

        when (restTemplateDocumentDto.documentStatus) {
            WfDocumentConstants.Status.USE.code -> {
                val wfFormEntity = wfFormRepository.findWfFormEntityByFormId(restTemplateDocumentDto.formId).get()
                if (wfFormEntity.formStatus != WfFormConstants.FormStatus.USE.value) {
                    wfFormEntity.formStatus = WfFormConstants.FormStatus.USE.value
                    wfFormRepository.save(wfFormEntity)
                }
                val wfProcessEntity =
                    wfProcessRepository.findByProcessId(restTemplateDocumentDto.processId) ?: throw AliceException(
                        AliceErrorConstants.ERR_00005,
                        AliceErrorConstants.ERR_00005.message + "[Process Entity]"
                    )
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
     * @return RestTemplateDocumentDisplayViewDto
     */
    fun getDocumentDisplay(documentId: String): RestTemplateDocumentDisplayViewDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val elementEntities = wfElementDataRepository.findElementDataByProcessId(documentEntity.process.processId)
        val componentEntities = wfComponentRepository.findByFormIdAndComponentTypeNot(documentEntity.form.formId, "editbox")
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
            val attributeValue = if (componentData.isNotEmpty()) {
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
        return RestTemplateDocumentDisplayViewDto(
            documentId = documentId,
            elements = elementEntities,
            components = components
        )
    }

    /**
     * Update Document Display data.
     *
     * @param restTemplateDocumentDisplaySaveDto
     * @return Boolean
     */
    fun updateDocumentDisplay(restTemplateDocumentDisplaySaveDto: RestTemplateDocumentDisplaySaveDto): Boolean {
        val documentId = restTemplateDocumentDisplaySaveDto.documentId
        wfDocumentDataRepository.deleteByDocumentId(documentId)
        val displays = restTemplateDocumentDisplaySaveDto.displays
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
