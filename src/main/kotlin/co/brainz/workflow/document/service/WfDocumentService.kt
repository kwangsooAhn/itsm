/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.document.service

import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.dto.CIDto
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.AliceUtil
import co.brainz.itsm.cmdb.ci.repository.CIComponentDataRepository
import co.brainz.itsm.numberingRule.repository.NumberingRuleRepository
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentDisplayEntity
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.document.repository.WfDocumentDisplayRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.element.service.WfActionService
import co.brainz.workflow.element.service.WfElementService
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.form.service.WfFormService
import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
import co.brainz.workflow.formGroup.repository.WfFormGroupRepository
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.provider.dto.DocumentSearchCondition
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentListDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import java.util.ArrayDeque
import kotlin.math.ceil
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
    private val wfDocumentDisplayRepository: WfDocumentDisplayRepository,
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfProcessRepository: WfProcessRepository,
    private val wfFormRepository: WfFormRepository,
    private val wfFormGroupRepository: WfFormGroupRepository,
    private val wfElementRepository: WfElementRepository,
    private val numberingRuleRepository: NumberingRuleRepository,
    private val aliceMessageSource: AliceMessageSource,
    private val ciService: CIService,
    private val ciComponentDataRepository: CIComponentDataRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Search Documents.
     *
     * @return List<RestTemplateDocumentDto>
     */
    fun documents(documentSearchCondition: DocumentSearchCondition): RestTemplateDocumentListReturnDto {
        val queryResult = wfDocumentRepository.findByDocuments(documentSearchCondition)
        val documentReturnList = mutableListOf<RestTemplateDocumentListDto>()
        for (data in queryResult.results) {
            val documentData = RestTemplateDocumentListDto(
                documentId = data.documentId,
                documentType = data.documentType,
                documentName = data.documentName,
                documentDesc = data.documentDesc,
                documentStatus = data.documentStatus,
                processId = data.process.processId,
                formId = data.form.formId,
                documentNumberingRuleId = data.numberingRule.numberingId,
                documentColor = data.documentColor,
                documentGroup = data.documentGroup,
                createUserKey = data.createUserKey,
                createDt = data.createDt,
                updateUserKey = data.updateUserKey,
                updateDt = data.updateDt,
                documentIcon = data.documentIcon
            )
            documentReturnList.add(documentData)
        }

        return RestTemplateDocumentListReturnDto(
            data = documentReturnList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = wfProcessRepository.count(),
                currentPageNum = documentSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * all Documents.
     *
     * @return List<RestTemplateDocumentDto>
     */
    fun allDocuments(searchListDto: DocumentSearchCondition): List<RestTemplateDocumentDto> {
        return wfDocumentRepository.findAllByDocuments(searchListDto)
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
        val firstElement = wfElementService.getNextElement(dummyTokenDto)
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
     * @param restTemplateDocumentDto
     * @return RestTemplateDocumentDto
     */
    @Transactional
    fun createDocument(restTemplateDocumentDto: RestTemplateDocumentDto): RestTemplateDocumentDto {
        val formId = restTemplateDocumentDto.formId
        val processId = restTemplateDocumentDto.processId
        val selectedForm = wfFormRepository.getOne(formId)
        val selectedProcess = wfProcessRepository.getOne(processId)
        val selectedDocument = wfDocumentRepository.findByFormAndProcess(selectedForm, selectedProcess)
        if (selectedDocument != null) {
            throw AliceException(
                AliceErrorConstants.ERR_00004,
                aliceMessageSource.getMessage("document.msg.checkDuplication")
            )
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
            apiEnable = restTemplateDocumentDto.apiEnable,
            numberingRule = numberingRuleRepository.findById(restTemplateDocumentDto.documentNumberingRuleId)
                .get(),
            documentColor = restTemplateDocumentDto.documentColor,
            documentGroup = restTemplateDocumentDto.documentGroup,
            documentIcon = restTemplateDocumentDto.documentIcon
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)
        this.createDocumentDisplay(dataEntity) // 신청서 양식 정보 초기화
        this.updateFormAndProcessStatus(dataEntity)

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
            documentColor = dataEntity.documentColor,
            documentGroup = dataEntity.documentGroup,
            documentIcon = dataEntity.documentIcon
        )
    }

    /**
     * Update Document.
     *
     * @param restTemplateDocumentDto
     * @return Boolean
     */
    @Transactional
    fun updateDocument(
        restTemplateDocumentDto: RestTemplateDocumentDto,
        params: LinkedHashMap<String, Any>
    ): Boolean {
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
        wfDocumentEntity.apiEnable = restTemplateDocumentDto.apiEnable
        wfDocumentEntity.numberingRule =
            numberingRuleRepository.findById(restTemplateDocumentDto.documentNumberingRuleId).get()
        wfDocumentEntity.documentColor = restTemplateDocumentDto.documentColor
        wfDocumentEntity.documentGroup = restTemplateDocumentDto.documentGroup
        wfDocumentEntity.documentIcon = restTemplateDocumentDto.documentIcon

        if (params["isDeleteData"].toString().toBoolean()) {
            logger.debug("Delete Instance Data... (Document Id: {})", wfDocumentEntity.documentId)
            val instanceIds = mutableListOf<String>()
            wfDocumentEntity.instance?.let { instances ->
                instances.forEach {
                    instanceIds.add(it.instanceId)
                }

                ciComponentDataRepository.findByInstanceIdIn(instanceIds)?.let { ciComponentDataList ->
                    ciComponentDataList.forEach { ciComponentData ->
                        val ciDto = CIDto(
                            ciId = ciComponentData.ciId,
                            typeId = "",
                            ciName = "",
                            ciStatus = ""
                        )
                        ciService.deleteCI(ciDto)
                    }
                }

                wfInstanceRepository.deleteInstances(instances)
            }
        }

        return true
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
     * @return Boolean
     */
    @Transactional
    fun updateDocumentDisplay(restTemplateDocumentDisplaySaveDto: RestTemplateDocumentDisplaySaveDto): Boolean {
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
        return true
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
}
