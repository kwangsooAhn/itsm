package co.brainz.workflow.document.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.numbering.repository.AliceNumberingRuleRepository
import co.brainz.workflow.component.repository.WfComponentDataRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentDisplayEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.repository.WfElementDataRepository
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.ArrayDeque
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

@Service
@Transactional
class WfDocumentService(
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val wfElementService: WfElementService,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfDocumentDisplayRepository: WfDocumentDisplayRepository,
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
            documentType = document.documentType,
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
     * 최초 신청서 작성용 데이터 반환.
     *
     * @author Jung Hee Chan
     * @since 2020-05-28
     * @param documentId
     * @return RestTemplateFormDocumentDto
     */
    fun getInitDocument(documentId: String): RestTemplateRequestDocumentDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)

        val form = wfFormService.getFormComponentList(documentEntity.form.formId)
        val dummyTokenDto =
            RestTemplateTokenDto(elementId = wfElementService.getStartElement(documentEntity.process.processId).elementId)
        val firstElement = wfElementService.getNextElement(dummyTokenDto)
        val documentDisplayList =
            wfDocumentDisplayRepository.findByDocumentIdAndElementId(documentId, firstElement.elementId)
        for (component in form.components) {
            for (documentDisplay in documentDisplayList) {
                if (component.componentId == documentDisplay.componentId) {
                    component.dataAttribute["displayType"] = documentDisplay.display
                }
            }
        }

        return RestTemplateRequestDocumentDto(
            documentId = documentId,
            form = form,
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
            documentType = restTemplateDocumentDto.documentType,
            documentName = restTemplateDocumentDto.documentName,
            documentDesc = restTemplateDocumentDto.documentDesc,
            form = form,
            process = process,
            createDt = restTemplateDocumentDto.createDt,
            createUserKey = restTemplateDocumentDto.createUserKey,
            documentStatus = restTemplateDocumentDto.documentStatus,
            numberingRule = aliceNumberingRuleRepository.findById(restTemplateDocumentDto.documentNumberingRuleId)
                .get(),
            documentColor = restTemplateDocumentDto.documentColor
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)

        // 신청서 양식 정보 초기화
        createDocumentDisplay(dataEntity)

        return RestTemplateDocumentDto(
            documentId = dataEntity.documentId,
            documentType = dataEntity.documentType,
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
        wfDocumentEntity.documentType = restTemplateDocumentDto.documentType
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
            wfDocumentDisplayRepository.deleteByDocumentId(documentId)
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
        val wfDocumentDisplayEntities: MutableList<WfDocumentDisplayEntity> = mutableListOf()
        val componentEntities = wfComponentRepository.findByFormId(documentEntity.form.formId)
        val elementEntities = wfElementRepository.findUserTaskByProcessId(documentEntity.process.processId)
        for (component in componentEntities) {
            for (element in elementEntities) {
                val documentDisplayEntity = WfDocumentDisplayEntity(
                    documentId = documentEntity.documentId,
                    componentId = component.componentId,
                    elementId = element.elementId
                )
                wfDocumentDisplayEntities.add(documentDisplayEntity)
            }
        }
        if (wfDocumentDisplayEntities.isNotEmpty()) {
            logger.debug("documentDisplay setting...")
            wfDocumentDisplayRepository.saveAll(wfDocumentDisplayEntities)
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
        val componentEntities =
            wfComponentRepository.findByFormIdAndComponentTypeNot(documentEntity.form.formId, "editbox")
        val displayList = wfDocumentDisplayRepository.findByDocumentId(documentId)

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
            val componentData =
                wfComponentDataRepository.findByComponentIdAndAttributeId(component.componentId, "label")
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
        wfDocumentDisplayRepository.deleteByDocumentId(documentId)
        val displays = restTemplateDocumentDisplaySaveDto.displays
        displays.forEach {
            wfDocumentDisplayRepository.save(
                WfDocumentDisplayEntity(
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
