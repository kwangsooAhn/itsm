/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.service

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.dto.CIDto
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
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
    private val ciService: CIService,
    private val ciComponentDataRepository: CIComponentDataRepository,
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
            documentGroup = document.documentGroup,
            documentIcon = document.documentIcon
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
            documentIcon = documentLink.documentIcon
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
     * @param documentDto
     */
    @Transactional
    fun createDocument(documentDto: DocumentDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var documentId = ""

        val formId = documentDto.formId
        val processId = documentDto.processId
        val selectedForm = wfFormRepository.getOne(formId)
        val selectedProcess = wfProcessRepository.getOne(processId)
        val selectedDocument = wfDocumentRepository.findByFormAndProcess(selectedForm, selectedProcess)
        if (selectedDocument != null) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE_WORKFLOW
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val isDuplicateName = wfDocumentRepository.existsByDocumentName(documentDto.documentName, documentDto.documentId)
            if (isDuplicateName) {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val form = WfFormEntity(formId = formId)
            val process = WfProcessEntity(processId = processId)

            val documentEntity = WfDocumentEntity(
                documentId = documentDto.documentId,
                documentType = documentDto.documentType,
                documentName = documentDto.documentName,
                documentDesc = documentDto.documentDesc,
                form = form,
                process = process,
                createDt = documentDto.createDt,
                createUserKey = documentDto.createUserKey,
                documentStatus = documentDto.documentStatus,
                apiEnable = documentDto.apiEnable,
                numberingRule = numberingRuleRepository.findById(documentDto.documentNumberingRuleId)
                    .get(),
                documentColor = documentDto.documentColor,
                documentGroup = documentDto.documentGroup,
                documentIcon = documentDto.documentIcon
            )

            val dataEntity = wfDocumentRepository.save(documentEntity)
            this.createDocumentDisplay(dataEntity) // 신청서 양식 정보 초기화
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
     * @param documentDto
     */
    @Transactional
    fun createDocumentLink(documentDto: DocumentDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val isDuplicateName = wfDocumentLinkRepository.existsByDocumentLinkName(documentDto.documentName, documentDto.documentId)
        if (isDuplicateName) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val documentLinkEntity = WfDocumentLinkEntity(
                documentLinkId = documentDto.documentId,
                documentName = documentDto.documentName,
                documentDesc = documentDto.documentDesc,
                documentStatus = documentDto.documentStatus,
                documentLinkUrl = documentDto.documentLinkUrl!!,
                documentColor = documentDto.documentColor,
                createDt = documentDto.createDt,
                createUserKey = documentDto.createUserKey,
                documentIcon = documentDto.documentIcon
            )
            wfDocumentLinkRepository.save(documentLinkEntity)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * Update Document.
     *
     * @param documentDto
     */
    @Transactional
    fun updateDocument(
        documentDto: DocumentDto,
        params: LinkedHashMap<String, Any>
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        val isDuplicateName = wfDocumentRepository.existsByDocumentName(documentDto.documentName, documentDto.documentId)
        if (isDuplicateName) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }

        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentDto.documentId)
            val form = WfFormEntity(formId = documentDto.formId)
            val process = WfProcessEntity(processId = documentDto.processId)

            wfDocumentEntity.documentType = documentDto.documentType
            wfDocumentEntity.documentName = documentDto.documentName
            wfDocumentEntity.documentDesc = documentDto.documentDesc
            wfDocumentEntity.documentStatus = documentDto.documentStatus
            wfDocumentEntity.updateUserKey = documentDto.updateUserKey
            wfDocumentEntity.updateDt = documentDto.updateDt
            wfDocumentEntity.form = form
            wfDocumentEntity.process = process
            wfDocumentEntity.apiEnable = documentDto.apiEnable
            wfDocumentEntity.numberingRule =
                numberingRuleRepository.findById(documentDto.documentNumberingRuleId).get()
            wfDocumentEntity.documentColor = documentDto.documentColor
            wfDocumentEntity.documentGroup = documentDto.documentGroup
            wfDocumentEntity.documentIcon = documentDto.documentIcon

            if (params["isDeleteData"].toString().toBoolean()) {
                this.deleteInstancesByStatusTemporary(wfDocumentEntity)

                // 신청서 사용시, 폼/프로세스 상태 변경
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
     * @param documentDto
     */
    @Transactional
    fun updateDocumentLink(
        documentDto: DocumentDto
    ): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val isDuplicateName = wfDocumentLinkRepository.existsByDocumentLinkName(documentDto.documentName, documentDto.documentId)
        if (isDuplicateName) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }

        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val wfDocumentLinkEntity = wfDocumentLinkRepository.findByDocumentLinkId(documentDto.documentId)
            wfDocumentLinkEntity.documentName = documentDto.documentName
            wfDocumentLinkEntity.documentDesc = documentDto.documentDesc
            wfDocumentLinkEntity.documentStatus = documentDto.documentStatus
            wfDocumentLinkEntity.documentLinkUrl = documentDto.documentLinkUrl.toString()
            wfDocumentLinkEntity.documentColor = documentDto.documentColor
            wfDocumentLinkEntity.documentIcon = documentDto.documentIcon
            wfDocumentLinkEntity.updateUserKey = documentDto.updateUserKey
            wfDocumentLinkEntity.updateDt = documentDto.updateDt
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
     * [elementEntities] 중 userTask 를 정렬하여 [List]로 반환
     */
    private fun makeSortedUserTasks(elementEntities: MutableList<WfElementEntity>): List<Map<String, Any>> {
        // userTask 를 저장할 변수
        val sortedUserTasks: MutableList<Map<String, Any>> = mutableListOf()
        // 게이트웨이가 가지는 컨넥터(화살표)를 저장할 큐
        val arrowConnectorsQueueInGateway: MutableMap<String, ArrayDeque<WfElementEntity>> = mutableMapOf()
        // 모든 컨넥터(화살표)를 꺼내서 체크된 gateway id 저장할 변수
        val checkedGatewayIds: MutableMap<String, String> = mutableMapOf()
        // gateway 에 여러개의 경우의 수가 있을 경우 저장
        val gatewayQueue = ArrayDeque<WfElementEntity>()
        // 쓸모 없는 그룹, 주석을 제거
        val allElementEntitiesInProcess =
            elementEntities.filter {
                WfElementConstants.ElementType.getAtomic(it.elementType) != WfElementConstants.ElementType.ARTIFACT
            }

        // 첫 commonStart 엘리먼트 찾기
        val startElement = allElementEntitiesInProcess.first {
            it.elementType == WfElementConstants.ElementType.COMMON_START_EVENT.value
        }
        var currentElement = startElement

        // while 을 돌면서 전체 프로세스 확인
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
     * 컴포넌트의 display 정보 설정
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
            // 강제적으로 component 가 추가 되었을때 기본값 출력
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
     * [currentElement]를 start-id 속성으로 가지는 컨넥터를 [allElementEntitiesInProcess]에서 찾아 리턴한다.
     * [currentElement]가 게이트웨이일 때는 [gatewayQueue]와 [arrowConnectorsQueueInGateway] 를 큐 형태로 관리하고
     * 컨넥터를 다 꺼내본 게이트웨이는 삭제하여 [checkedGatewayIds] 에 기록한다.
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

            // 컨넥터를 모두 꺼내서 사용 했으면 해당 gateway 는 삭제하고 allCheckedGatewayIds 에 기록한다.
            if (arrowConnectorsQueueInGateway[currentElement.elementId]!!.size == 0) {
                arrowConnectorsQueueInGateway.remove(currentElement.elementId)
                gatewayQueue.remove(currentElement)
                checkedGatewayIds[currentElement.elementId] = currentElement.elementId
            } else {
                // 게이트웨이가 가지고 있는 컨넥터(화살표) 를 모두 찾아쓰고 확인할 필요가 없는 게이트웨이인지 확인 후
                // 아닌 경우 사용하지 않은 컨넥터(화살표)를 꺼내기위해 게이트웨이를 큐에 다시 넣어 둔다.
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
     * [arrowConnector] 의 end-id 가 가르키는 타겟 엘리먼트를 찾아서 리턴한다.
     * 타겟 엘리먼트가 종료 엘리먼트인 경우 최근 게이트웨이를 찾아 리턴한다.
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
     * [currentElement] 가 userTask 이면  [sortedUserTasks] 에 저장한다.
     * 게이트웨이의 컨넥터 개수에 따라 중복 저장이 가능하므로 존재하는 경우 삭제 후 다시 저장
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
     * 업무흐름 Import.
     * 신규로 Import된 프로세스, 폼은 문서가 '임시'일 경우 '발행' 상태이고 '사용'일 경우 사용 상태이다.
     */
    @Transactional
    fun importDocument(documentImportDto: DocumentImportDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        var message = ""
        var importDto: DocumentImportDto
        // 폼 중복 체크
        if (wfFormRepository.existsByFormName(documentImportDto.formData.name)) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            message = "form"
        }

        // 프로세스 중복 체크
        if (status == ZResponseConstants.STATUS.SUCCESS &&
            wfProcessRepository.existsByProcessName(documentImportDto.processData.process!!.name!!)
        ) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            message = "process"
        }

        // 문서 중복 체크
        if (status == ZResponseConstants.STATUS.SUCCESS &&
            wfDocumentRepository.existsByDocumentName(documentImportDto.documentData.documentName, "")
        ) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            message = "document"
        }

        // 유효성 검증 후 처리
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            // 폼 저장
            importDto = this.importForm(documentImportDto)
            val newFormId = importDto.formData.id.toString()

            // 프로세스 저장
            importDto = this.importProcess(importDto)
            val newProcessId = importDto.processData.process?.id!!

            // 신청서 저장
            val newDocumentId = wfDocumentRepository.save(
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
                    documentGroup = importDto.documentData.documentGroup,
                    documentIcon = importDto.documentData.documentIcon
                )
            ).documentId
            // 신청서 양식 편집 저장
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
            message = message
        )
    }

    /**
     * 업무흐름 Import - 폼.
     * WfFormService.kt - saveAsFormData 참고
     */
    private fun importForm(documentImportDto: DocumentImportDto): DocumentImportDto {
        documentImportDto.formData.id = ""
        //신규로 Import된 폼은 문서가 '임시'일 경우 발행 상태이고 '사용'일 경우 사용 상태이다.
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
        // 변경된 Group Id를 신청서 양식 편집에도 반영함
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
     * 업무흐름 Import - 프로세스.
     * WfProcessService.kt - saveAsProcess 참고
     */
    private fun importProcess(documentImportDto: DocumentImportDto): DocumentImportDto {
        // 신규로 Import된 프로세스는 문서가 '임시'일 경우 발행 상태이고 '사용'일 경우 사용 상태이다.
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
        // 변경된 Element Id를 신청서 양식 편집에도 반영함
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
     * 이력 조회 컴포넌트 데이터 조회
     */
    fun getSearchFieldValues(filedOption: FieldOptionDto): List<Array<Any>> {
        return wfDocumentRepository.getSearchFieldValues(filedOption)
    }

    /**
     * 임시상태 수정 및 삭제 시 인스턴스 제거
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
