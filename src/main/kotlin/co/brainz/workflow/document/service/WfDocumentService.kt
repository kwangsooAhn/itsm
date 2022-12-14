/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
import co.brainz.itsm.document.dto.DocumentEditDto
import co.brainz.itsm.document.dto.DocumentImportDto
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.dto.FieldOptionDto
import co.brainz.itsm.numberingRule.repository.NumberingRuleRepository
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentDisplayEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.entity.WfDocumentLinkEntity
import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.document.repository.WfDocumentLinkRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.mapper.WfFormMapper
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
import co.brainz.workflow.formGroup.repository.WfFormGroupRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.mapper.WfProcessMapper
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.process.service.WfProcessService
import co.brainz.workflow.provider.constants.WorkflowConstants
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateElementDto
import co.brainz.workflow.provider.dto.RestTemplateProcessDto
import co.brainz.workflow.provider.dto.RestTemplateProcessElementDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import java.time.LocalDateTime
import java.util.ArrayDeque
import java.util.UUID
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class WfDocumentService(
    private val wfFormService: WfFormService,
    private val wfActionService: WfActionService,
    private val wfElementService: WfElementService,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfDocumentLinkRepository: WfDocumentLinkRepository,
    private val wfDocumentDisplayRepository: WfDocumentDisplayRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfProcessRepository: WfProcessRepository,
    private val wfFormRepository: WfFormRepository,
    private val wfFormGroupRepository: WfFormGroupRepository,
    private val wfElementRepository: WfElementRepository,
    private val numberingRuleRepository: NumberingRuleRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val wfProcessService: WfProcessService
) {
    private val wfFormMapper = Mappers.getMapper(WfFormMapper::class.java)
    private val wfProcessMapper = Mappers.getMapper(WfProcessMapper::class.java)

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * all Documents.
     *
     * @return List<RestTemplateDocumentDto>
     */
    fun allDocuments(searchListDto: DocumentSearchCondition): List<DocumentDto> {
        return wfDocumentRepository.findAllByDocuments(searchListDto)
    }

    /**
     * Search Document.
     *
     * @param documentId
     * @return RestTemplateDocumentDto
     */
    fun getDocument(documentId: String): DocumentDto {
        val document = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        return DocumentDto(
            documentId = document.documentId,
            documentType = document.documentType,
            documentName = document.documentName,
            documentDesc = document.documentDesc,
            documentStatus = document.documentStatus,
            processId = document.process.processId,
            formId = document.form.formId,
            apiEnable = document.apiEnable,
            createDt = document.createDt,
            createUserKey = document.createUserKey,
            updateDt = document.updateDt,
            updateUserKey = document.updateUserKey,
            documentNumberingRuleId = document.numberingRule.numberingId,
            documentColor = document.documentColor,
            documentGroup = document.documentGroup
        )
    }

    /**
     * Search Document.
     *
     * @param documentId
     * @return DocumentDto
     */
    fun getDocumentLink(documentId: String): DocumentDto {
        val documentLink = wfDocumentLinkRepository.findByDocumentLinkId(documentId)
        return DocumentDto(
            documentId = documentLink.documentLinkId,
            documentType = DocumentConstants.DocumentType.APPLICATION_FORM_LINK.value,
            documentName = documentLink.documentName,
            documentDesc = documentLink.documentDesc,
            documentLinkUrl = documentLink.documentLinkUrl,
            documentStatus = documentLink.documentStatus,
            processId = "",
            formId = "",
            apiEnable = false,
            createDt = documentLink.createDt,
            createUserKey = documentLink.createUserKey,
            updateDt = documentLink.updateDt,
            updateUserKey = documentLink.updateUserKey,
            documentNumberingRuleId = "",
            documentColor = documentLink.documentColor,
            documentGroup = documentLink.documentGroup
        )
    }

    /**
     * ?????? ????????? ????????? ????????? ??????.
     *
     * @author Jung Hee Chan
     * @since 2020-05-28
     * @param documentId
     * @return RestTemplateFormDocumentDto
     */
    fun getInitDocument(documentId: String): RestTemplateRequestDocumentDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)

        val form = wfFormService.getFormData(documentEntity.form.formId)
        val dummyTokenDto =
            WfTokenDto(elementId = wfElementService.getStartElement(documentEntity.process.processId).elementId)
        val firstElement = wfElementService.getFirstUserTaskElement(dummyTokenDto)
        val documentDisplayList =
            wfDocumentDisplayRepository.findByDocumentIdAndElementId(documentId, firstElement.elementId)

        form.group?.let {
            for (group in form.group) {
                for (documentDisplay in documentDisplayList) {
                    if (group.id == documentDisplay.formGroupId) {
                        group.displayType = documentDisplay.display
                    }
                }
            }
        }

        return RestTemplateRequestDocumentDto(
            documentId = documentId,
            instanceId = AliceUtil().getUUID(),
            form = form,
            actions = wfActionService.actionInit(documentEntity.process.processId)
        )
    }

    /**
     * Create Document.
     *
     * @param documentEditDto
     */
    @Transactional
    fun createDocument(documentEditDto: DocumentEditDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var documentId = ""

        val formId = documentEditDto.formId
        val processId = documentEditDto.processId
        val selectedForm = wfFormRepository.getOne(formId)
        val selectedProcess = wfProcessRepository.getOne(processId)
        val selectedDocument = wfDocumentRepository.findByFormAndProcess(selectedForm, selectedProcess)
        if (selectedDocument != null) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE_WORKFLOW
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val isDuplicateName = wfDocumentRepository.existsByDocumentName(documentEditDto.documentName, documentEditDto.documentId)
            if (isDuplicateName) {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val form = WfFormEntity(formId = formId)
            val process = WfProcessEntity(processId = processId)

            val documentEntity = WfDocumentEntity(
                documentId = documentEditDto.documentId,
                documentType = documentEditDto.documentType,
                documentName = documentEditDto.documentName,
                documentDesc = documentEditDto.documentDesc,
                form = form,
                process = process,
                createDt = documentEditDto.createDt,
                createUserKey = documentEditDto.createUserKey,
                documentStatus = documentEditDto.documentStatus,
                apiEnable = documentEditDto.apiEnable,
                numberingRule = numberingRuleRepository.findById(documentEditDto.documentNumberingRuleId)
                    .get(),
                documentColor = documentEditDto.documentColor,
                documentGroup = documentEditDto.documentGroup
            )

            val dataEntity = wfDocumentRepository.save(documentEntity)
            this.createDocumentDisplay(dataEntity) // ????????? ?????? ?????? ?????????
            this.updateFormAndProcessStatus(dataEntity)

            documentId = dataEntity.documentId
        }

        return ZResponse(
            status = status.code,
            data = documentId
        )
    }

    /**
     * Create DocumentLink.
     *
     * @param documentEditDto
     */
    @Transactional
    fun createDocumentLink(documentEditDto: DocumentEditDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var documentLinkId = ""

        val isDuplicateName = wfDocumentLinkRepository.existsByDocumentLinkName(documentEditDto.documentName, documentEditDto.documentId)
        if (isDuplicateName) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val documentLinkEntity = WfDocumentLinkEntity(
                documentLinkId = documentEditDto.documentId,
                documentName = documentEditDto.documentName,
                documentDesc = documentEditDto.documentDesc,
                documentStatus = documentEditDto.documentStatus,
                documentLinkUrl = documentEditDto.documentLinkUrl!!,
                documentColor = documentEditDto.documentColor,
                documentGroup = documentEditDto.documentGroup,
                createDt = documentEditDto.createDt,
                createUserKey = documentEditDto.createUserKey
            )
            val dataEntity = wfDocumentLinkRepository.save(documentLinkEntity)

            documentLinkId = dataEntity.documentLinkId
        }
        return ZResponse(
            status = status.code,
            data = documentLinkId
        )
    }

    /**
     * Update Document.
     *
     * @param documentEditDto
     */
    @Transactional
    fun updateDocument(
        documentEditDto: DocumentEditDto,
        params: LinkedHashMap<String, Any>
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        val isDuplicateName = wfDocumentRepository.existsByDocumentName(documentEditDto.documentName, documentEditDto.documentId)
        if (isDuplicateName) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }

        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentEditDto.documentId)
            val form = WfFormEntity(formId = documentEditDto.formId)
            val process = WfProcessEntity(processId = documentEditDto.processId)

            wfDocumentEntity.documentType = documentEditDto.documentType
            wfDocumentEntity.documentName = documentEditDto.documentName
            wfDocumentEntity.documentDesc = documentEditDto.documentDesc
            wfDocumentEntity.documentStatus = documentEditDto.documentStatus
            wfDocumentEntity.updateUserKey = documentEditDto.updateUserKey
            wfDocumentEntity.updateDt = documentEditDto.updateDt
            wfDocumentEntity.form = form
            wfDocumentEntity.process = process
            wfDocumentEntity.apiEnable = documentEditDto.apiEnable
            wfDocumentEntity.numberingRule =
                numberingRuleRepository.findById(documentEditDto.documentNumberingRuleId).get()
            wfDocumentEntity.documentColor = documentEditDto.documentColor
            wfDocumentEntity.documentGroup = documentEditDto.documentGroup

            if (params["isDeleteData"].toString().toBoolean()) {
                this.deleteInstancesByStatusTemporary(wfDocumentEntity)

                // ????????? ?????????, ???/???????????? ?????? ??????
                this.updateFormAndProcessStatus(wfDocumentEntity)
            }
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * Update DocumentLink.
     *
     * @param documentEditDto
     */
    @Transactional
    fun updateDocumentLink(
        documentEditDto: DocumentEditDto
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val isDuplicateName = wfDocumentLinkRepository.existsByDocumentLinkName(documentEditDto.documentName, documentEditDto.documentId)
        if (isDuplicateName) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }

        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val wfDocumentLinkEntity = wfDocumentLinkRepository.findByDocumentLinkId(documentEditDto.documentId)
            wfDocumentLinkEntity.documentName = documentEditDto.documentName
            wfDocumentLinkEntity.documentDesc = documentEditDto.documentDesc
            wfDocumentLinkEntity.documentStatus = documentEditDto.documentStatus
            wfDocumentLinkEntity.documentLinkUrl = documentEditDto.documentLinkUrl.toString()
            wfDocumentLinkEntity.documentColor = documentEditDto.documentColor
            wfDocumentLinkEntity.documentGroup = documentEditDto.documentGroup
            wfDocumentLinkEntity.updateUserKey = documentEditDto.updateUserKey
            wfDocumentLinkEntity.updateDt = documentEditDto.updateDt
        }

        return ZResponse(
            status = status.code
        )
    }

    fun getDocumentListByNumberingId(numberingId: String): List<WfDocumentEntity> {
        return wfDocumentRepository.getDocumentListByNumberingId(numberingId)
    }

    /**
     * Update Form and Process status.
     */
    private fun updateFormAndProcessStatus(documentEntity: WfDocumentEntity) {
        when (documentEntity.documentStatus) {
            WfDocumentConstants.Status.USE.code -> {
                val wfFormEntity = wfFormRepository.findWfFormEntityByFormId(documentEntity.form.formId).get()
                if (wfFormEntity.formStatus != WfFormConstants.FormStatus.USE.value) {
                    wfFormEntity.formStatus = WfFormConstants.FormStatus.USE.value
                    wfFormRepository.save(wfFormEntity)
                }
                val wfProcessEntity = wfProcessRepository.findByProcessId(documentEntity.process.processId)
                    ?: throw AliceException(
                        AliceErrorConstants.ERR_00005,
                        AliceErrorConstants.ERR_00005.message + "[Process Entity]"
                    )
                if (wfProcessEntity.processStatus != WfProcessConstants.Status.USE.code) {
                    wfProcessEntity.processStatus = WfProcessConstants.Status.USE.code
                    wfProcessRepository.save(wfProcessEntity)
                }
            }
        }
    }

    /**
     * Delete Document.
     *
     * @param documentId
     * @return Boolean
     */
    @Transactional
    fun deleteDocument(documentId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val selectedDocument = wfDocumentRepository.getOne(documentId)
        val instanceCnt = wfInstanceRepository.countByDocument(selectedDocument)
        if (instanceCnt == 0 || selectedDocument.documentStatus == DocumentConstants.DocumentStatus.TEMPORARY.value) {
            logger.debug("Try delete document...")
            this.deleteInstancesByStatusTemporary(selectedDocument)
            wfDocumentDisplayRepository.deleteByDocumentId(documentId)
            wfDocumentRepository.deleteByDocumentId(documentId)
        } else {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * Delete DocumentLink.
     *
     * @param documentId
     */
    @Transactional
    fun deleteDocumentLink(documentId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (wfDocumentLinkRepository.deleteByDocumentLinkId(documentId) != 1) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * Create Document Display.
     *
     * @param documentEntity
     */
    private fun createDocumentDisplay(documentEntity: WfDocumentEntity) {
        val wfDocumentDisplayEntities: MutableList<WfDocumentDisplayEntity> = mutableListOf()
        val formGroupEntities = wfFormGroupRepository.findByFormId(documentEntity.form.formId)
        val elementEntities = wfElementRepository.findUserTaskByProcessId(documentEntity.process.processId)
        for (formGroup in formGroupEntities) {
            for (element in elementEntities) {
                val documentDisplayEntity = WfDocumentDisplayEntity(
                    documentId = documentEntity.documentId,
                    formGroupId = formGroup.formGroupId,
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
     * [elementEntities] ??? userTask ??? ???????????? [List]??? ??????
     */
    private fun makeSortedUserTasks(elementEntities: MutableList<WfElementEntity>): List<Map<String, Any>> {
        // userTask ??? ????????? ??????
        val sortedUserTasks: MutableList<Map<String, Any>> = mutableListOf()
        // ?????????????????? ????????? ?????????(?????????)??? ????????? ???
        val arrowConnectorsQueueInGateway: MutableMap<String, ArrayDeque<WfElementEntity>> = mutableMapOf()
        // ?????? ?????????(?????????)??? ????????? ????????? gateway id ????????? ??????
        val checkedGatewayIds: MutableMap<String, String> = mutableMapOf()
        // gateway ??? ???????????? ????????? ?????? ?????? ?????? ??????
        val gatewayQueue = ArrayDeque<WfElementEntity>()
        // ?????? ?????? ??????, ????????? ??????
        val allElementEntitiesInProcess =
            elementEntities.filter {
                WfElementConstants.ElementType.getAtomic(it.elementType) != WfElementConstants.ElementType.ARTIFACT
            }

        // ??? commonStart ???????????? ??????
        val startElement = allElementEntitiesInProcess.first {
            it.elementType == WfElementConstants.ElementType.COMMON_START_EVENT.value
        }
        var currentElement = startElement

        // while ??? ????????? ?????? ???????????? ??????
        while (currentElement.elementType != WfElementConstants.ElementType.COMMON_END_EVENT.value) {
            val arrowConnector = getArrowConnector(
                allElementEntitiesInProcess,
                currentElement,
                arrowConnectorsQueueInGateway,
                gatewayQueue,
                checkedGatewayIds
            )

            currentElement = changeCurrentElementToNextElement(
                allElementEntitiesInProcess,
                arrowConnector,
                gatewayQueue
            )

            ifUserTaskSave(currentElement, sortedUserTasks)
        }
        return sortedUserTasks
    }

    /**
     * ??????????????? display ?????? ??????
     */
    private fun getDisplayGroup(
        userTasks: List<Map<String, Any>>,
        displayList: List<WfDocumentDisplayEntity>,
        formGroup: WfFormGroupEntity
    ): LinkedHashMap<String, Any> {
        var isDisplay = false
        val displayValue: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (elementEntity in userTasks) {
            for (display in displayList) {
                if (display.formGroupId == formGroup.formGroupId &&
                    display.elementId == elementEntity["elementId"].toString()
                ) {
                    val displayMap = LinkedHashMap<String, Any>()
                    displayMap["elementId"] = display.elementId
                    displayMap["display"] = display.display
                    displayValue.add(displayMap)
                    isDisplay = true
                }
            }
            // ??????????????? component ??? ?????? ???????????? ????????? ??????
            if (!isDisplay) {
                val displayMap = LinkedHashMap<String, Any>()
                displayMap["elementId"] = elementEntity["elementId"].toString()
                displayMap["display"] = WfDocumentConstants.DisplayType.EDITABLE.value
                displayValue.add(displayMap)
                isDisplay = false
            }
        }
        val formGroupMap = LinkedHashMap<String, Any>()
        formGroupMap["formGroupId"] = formGroup.formGroupId
        formGroupMap["formGroupName"] = formGroup.formGroupName
        formGroupMap["displayValue"] = displayValue
        return formGroupMap
    }

    /**
     * Search Document Display data.
     *
     * @param documentId
     * @return RestTemplateDocumentDisplayViewDto
     */
    fun getDocumentDisplay(documentId: String): RestTemplateDocumentDisplayViewDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val userTasks = this.makeSortedUserTasks(documentEntity.process.elementEntities)
        val formGroupEntities = wfFormGroupRepository.findByFormId(documentEntity.form.formId)
        val formGroups: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (formGroup in formGroupEntities) {
            formGroups.add(this.getDisplayGroup(userTasks, documentEntity.display, formGroup))
        }
        return RestTemplateDocumentDisplayViewDto(
            documentId = documentId,
            elements = userTasks,
            formGroups = formGroups
        )
    }

    /**
     * Update Document Display data.
     *
     * @param restTemplateDocumentDisplaySaveDto
     */
    @Transactional
    fun updateDocumentDisplay(restTemplateDocumentDisplaySaveDto: RestTemplateDocumentDisplaySaveDto): ZResponse {
        val documentId = restTemplateDocumentDisplaySaveDto.documentId
        wfDocumentDisplayRepository.deleteByDocumentId(documentId)
        val displays = restTemplateDocumentDisplaySaveDto.displays
        displays.forEach {
            wfDocumentDisplayRepository.save(
                WfDocumentDisplayEntity(
                    documentId = documentId,
                    formGroupId = it.getValue("formGroupId").toString(),
                    elementId = it.getValue("elementId").toString(),
                    display = it.getValue("display").toString()
                )
            )
        }
        return ZResponse()
    }

    /**
     * [currentElement]??? start-id ???????????? ????????? ???????????? [allElementEntitiesInProcess]?????? ?????? ????????????.
     * [currentElement]??? ?????????????????? ?????? [gatewayQueue]??? [arrowConnectorsQueueInGateway] ??? ??? ????????? ????????????
     * ???????????? ??? ????????? ?????????????????? ???????????? [checkedGatewayIds] ??? ????????????.
     */
    private fun getArrowConnector(
        allElementEntitiesInProcess: List<WfElementEntity>,
        currentElement: WfElementEntity,
        arrowConnectorsQueueInGateway: MutableMap<String, ArrayDeque<WfElementEntity>>,
        gatewayQueue: ArrayDeque<WfElementEntity>,
        checkedGatewayIds: MutableMap<String, String>
    ): WfElementEntity {
        val arrowConnectors = allElementEntitiesInProcess.filter {
            currentElement.elementId == it.getElementDataValue(WfElementConstants.AttributeId.SOURCE_ID.value)
        }

        val arrowConnector: WfElementEntity
        if (currentElement.elementType == WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value) {
            if (arrowConnectorsQueueInGateway[currentElement.elementId] == null) {
                arrowConnectorsQueueInGateway[currentElement.elementId] = ArrayDeque(arrowConnectors)
            }
            arrowConnector = arrowConnectorsQueueInGateway[currentElement.elementId]!!.pop()

            // ???????????? ?????? ????????? ?????? ????????? ?????? gateway ??? ???????????? allCheckedGatewayIds ??? ????????????.
            if (arrowConnectorsQueueInGateway[currentElement.elementId]!!.size == 0) {
                arrowConnectorsQueueInGateway.remove(currentElement.elementId)
                gatewayQueue.remove(currentElement)
                checkedGatewayIds[currentElement.elementId] = currentElement.elementId
            } else {
                // ?????????????????? ????????? ?????? ?????????(?????????) ??? ?????? ???????????? ????????? ????????? ?????? ????????????????????? ?????? ???
                // ?????? ?????? ???????????? ?????? ?????????(?????????)??? ??????????????? ?????????????????? ?????? ?????? ?????? ??????.
                if (checkedGatewayIds[currentElement.elementId] != currentElement.elementId) {
                    gatewayQueue.push(currentElement)
                }
            }
        } else {
            arrowConnector = arrowConnectors.last()
        }

        return arrowConnector
    }

    /**
     * [arrowConnector] ??? end-id ??? ???????????? ?????? ??????????????? ????????? ????????????.
     * ?????? ??????????????? ?????? ??????????????? ?????? ?????? ?????????????????? ?????? ????????????.
     */
    private fun changeCurrentElementToNextElement(
        allElementEntitiesInProcess: List<WfElementEntity>,
        arrowConnector: WfElementEntity,
        gatewayQueue: ArrayDeque<WfElementEntity>
    ): WfElementEntity {
        val targetElementId = arrowConnector.getElementDataValue(WfElementConstants.AttributeId.TARGET_ID.value)
        var nextElement = allElementEntitiesInProcess.first {
            it.elementId == targetElementId
        }

        if (gatewayQueue.size > 0 && nextElement.elementType == WfElementConstants.ElementType.COMMON_END_EVENT.value) {
            nextElement = gatewayQueue.pop()
        }

        return nextElement
    }

    /**
     * [currentElement] ??? userTask ??????  [sortedUserTasks] ??? ????????????.
     * ?????????????????? ????????? ????????? ?????? ?????? ????????? ??????????????? ???????????? ?????? ?????? ??? ?????? ??????
     */
    private fun ifUserTaskSave(currentElement: WfElementEntity, sortedUserTasks: MutableList<Map<String, Any>>) {
        if (currentElement.elementType == WfElementConstants.ElementType.USER_TASK.value) {
            val elementAttribute = LinkedHashMap<String, Any>()
            elementAttribute["elementId"] = currentElement.elementId
            if (currentElement.elementName != "") {
                elementAttribute["elementName"] = currentElement.elementName
            } else {
                elementAttribute["elementName"] = currentElement.elementType
            }
            if (sortedUserTasks.contains(elementAttribute)) {
                sortedUserTasks.remove(elementAttribute)
            }
            sortedUserTasks.add(elementAttribute)
        }
    }

    /**
     * ???????????? Import.
     * ????????? Import??? ????????????, ?????? ????????? '??????'??? ?????? '??????' ???????????? '??????'??? ?????? ?????? ????????????.
     */
    @Transactional
    fun importDocument(documentImportDto: DocumentImportDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var newDocumentId = ""
        var message = ""
        var importDto: DocumentImportDto
        // ??? ?????? ??????
        if (wfFormRepository.existsByFormName(documentImportDto.formData.name)) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            message = "form"
        }

        // ???????????? ?????? ??????
        if (status == ZResponseConstants.STATUS.SUCCESS &&
            wfProcessRepository.existsByProcessName(documentImportDto.processData.process!!.name!!)
        ) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            message = "process"
        }

        // ?????? ?????? ??????
        if (status == ZResponseConstants.STATUS.SUCCESS &&
            wfDocumentRepository.existsByDocumentName(documentImportDto.documentData.documentName, "")
        ) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            message = "document"
        }

        // ????????? ?????? ??? ??????
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            // ??? ??????
            importDto = this.importForm(documentImportDto)
            val newFormId = importDto.formData.id.toString()

            // ???????????? ??????
            importDto = this.importProcess(importDto)
            val newProcessId = importDto.processData.process?.id!!

            // ????????? ??????
            newDocumentId = wfDocumentRepository.save(
                WfDocumentEntity(
                    documentId = "",
                    documentType = importDto.documentData.documentType,
                    documentName = importDto.documentData.documentName,
                    documentDesc = importDto.documentData.documentDesc,
                    form = WfFormEntity(formId = newFormId),
                    process = WfProcessEntity(processId = newProcessId),
                    createDt = LocalDateTime.now(),
                    createUserKey = currentSessionUser.getUserKey(),
                    documentStatus = importDto.documentData.documentStatus,
                    apiEnable = importDto.documentData.apiEnable,
                    numberingRule = numberingRuleRepository.findById(importDto.documentData.documentNumberingRuleId).get(),
                    documentColor = importDto.documentData.documentColor,
                    documentGroup = importDto.documentData.documentGroup
                )
            ).documentId
            // ????????? ?????? ?????? ??????
            val wfDocumentDisplayEntities: MutableList<WfDocumentDisplayEntity> = mutableListOf()
            for (display in importDto.displayData) {
                val documentDisplayEntity = WfDocumentDisplayEntity(
                    documentId = newDocumentId,
                    formGroupId = display["formGroupId"] as String,
                    elementId = display["elementId"] as String,
                    display = display["display"] as String
                )
                wfDocumentDisplayEntities.add(documentDisplayEntity)
            }
            if (wfDocumentDisplayEntities.isNotEmpty()) {
                wfDocumentDisplayRepository.saveAll(wfDocumentDisplayEntities)
            }
        }
        return ZResponse(
            status = status.code,
            data = newDocumentId,
            message = message
        )
    }

    /**
     * ???????????? Import - ???.
     * WfFormService.kt - saveAsFormData ??????
     */
    private fun importForm(documentImportDto: DocumentImportDto): DocumentImportDto {
        documentImportDto.formData.id = ""
        // ????????? Import??? ?????? ????????? '??????'??? ?????? ?????? ???????????? '??????'??? ?????? ?????? ????????????.
        documentImportDto.formData.status = when (documentImportDto.documentData.documentStatus) {
            DocumentConstants.DocumentStatus.TEMPORARY.value -> WorkflowConstants.FormStatus.PUBLISH.value
            else -> WorkflowConstants.FormStatus.USE.value
        }
        documentImportDto.formData.createUserKey = currentSessionUser.getUserKey()
        documentImportDto.formData.createDt = LocalDateTime.now()

        val wfFormDto = wfFormService.createForm(
            wfFormMapper.toRestTemplateFormDto(documentImportDto.formData)
        )
        documentImportDto.formData.id = wfFormDto.id
        documentImportDto.documentData.formId = wfFormDto.id
        // ????????? Group Id??? ????????? ?????? ???????????? ?????????
        for (group in documentImportDto.formData.group.orEmpty()) {
            if (group.id.isNotBlank()) {
                val newGroupId = UUID.randomUUID().toString().replace("-", "")
                for (display in documentImportDto.displayData) {
                    if (display["formGroupId"] == group.id) {
                        display["formGroupId"] = newGroupId
                    }
                }
                group.id = newGroupId
                for (row in group.row) {
                    row.id = UUID.randomUUID().toString().replace("-", "")
                    for (component in row.component) {
                        component.id = UUID.randomUUID().toString().replace("-", "")
                    }
                }
            }
        }
        wfFormService.saveFormData(documentImportDto.formData)
        return documentImportDto
    }

    /**
     * ???????????? Import - ????????????.
     * WfProcessService.kt - saveAsProcess ??????
     */
    private fun importProcess(documentImportDto: DocumentImportDto): DocumentImportDto {
        // ????????? Import??? ??????????????? ????????? '??????'??? ?????? ?????? ???????????? '??????'??? ?????? ?????? ????????????.
        val processStatus = when (documentImportDto.documentData.documentStatus) {
            DocumentConstants.DocumentStatus.TEMPORARY.value -> WorkflowConstants.ProcessStatus.PUBLISH.value
            else -> WorkflowConstants.ProcessStatus.USE.value
        }
        val processDto = wfProcessService.insertProcess(
            RestTemplateProcessDto(
                processName = documentImportDto.processData.process?.name.toString(),
                processDesc = documentImportDto.processData.process?.description,
                processStatus = processStatus,
                enabled = false,
                createDt = LocalDateTime.now(),
                createUserKey = currentSessionUser.getUserKey()
            )
        )
        val newProcess = wfProcessMapper.toRestTemplateProcessViewDto(processDto)
        documentImportDto.documentData.processId = processDto.processId
        val newElements: MutableList<RestTemplateElementDto> = mutableListOf()
        val elementKeyMap: MutableMap<String, String> = mutableMapOf()
        // ????????? Element Id??? ????????? ?????? ???????????? ?????????
        documentImportDto.processData.elements?.forEach { element ->
            val newElementId = UUID.randomUUID().toString().replace("-", "")
            for (display in documentImportDto.displayData) {
                if (display["elementId"] == element.id) {
                    display["elementId"] = newElementId
                }
            }
            elementKeyMap[element.id] = newElementId
        }
        documentImportDto.processData.elements?.forEach { element ->
            val dataMap: MutableMap<String, Any>? = element.data
            dataMap?.forEach {
                if (elementKeyMap.containsKey(it.value)) {
                    dataMap[it.key] = elementKeyMap[it.value].toString()
                }
            }

            newElements.add(
                RestTemplateElementDto(
                    id = elementKeyMap[element.id]!!,
                    name = element.name,
                    description = element.description,
                    notification = element.notification,
                    data = dataMap,
                    type = element.type,
                    display = element.display
                )
            )
        }
        documentImportDto.processData = RestTemplateProcessElementDto(
            process = newProcess,
            elements = newElements
        )
        wfProcessService.updateProcessData(documentImportDto.processData)
        return documentImportDto
    }

    /**
     * ?????? ?????? ???????????? ????????? ??????
     */
    fun getSearchFieldValues(filedOption: FieldOptionDto): List<Array<Any>> {
        return wfDocumentRepository.getSearchFieldValues(filedOption)
    }

    /**
     * ???????????? ?????? ??? ?????? ??? ???????????? ??????
     */
    private fun deleteInstancesByStatusTemporary(wfDocumentEntity: WfDocumentEntity) {
        logger.debug("Delete Instance Data... (Document Id: {})", wfDocumentEntity.documentId)
        val instanceIds = mutableListOf<String>()
        wfDocumentEntity.instance?.let { instances ->
            instances.forEach {
                instanceIds.add(it.instanceId)
            }
            wfInstanceRepository.deleteInstances(instances)
        }
    }
}
