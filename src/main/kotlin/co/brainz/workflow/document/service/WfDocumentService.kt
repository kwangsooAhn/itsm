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
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.process.constants.WfProcessConstants
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.process.repository.WfProcessRepository
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplaySaveDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDisplayViewDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentDto
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import java.util.ArrayDeque
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
    private val wfComponentRepository: WfComponentRepository,
    private val wfComponentDataRepository: WfComponentDataRepository,
    private val wfElementRepository: WfElementRepository,
    private val aliceNumberingRuleRepository: AliceNumberingRuleRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Search Documents.
     *
     * @return List<RestTemplateDocumentDto>
     */
    fun documents(searchListDto: RestTemplateDocumentSearchListDto): List<RestTemplateDocumentDto> {
        val queryResult = wfDocumentRepository.findByDocuments(searchListDto)
        val documentList = mutableListOf<RestTemplateDocumentDto>()
        for (document in queryResult.results) {
            documentList.add(
                RestTemplateDocumentDto(
                    documentId = document.documentId,
                    documentType = document.documentType,
                    documentName = document.documentName,
                    documentDesc = document.documentDesc,
                    documentStatus = document.documentStatus,
                    processId = document.process.processId,
                    formId = document.form.formId,
                    documentNumberingRuleId = document.numberingRule.numberingId,
                    documentColor = document.documentColor,
                    documentGroup = document.documentGroup,
                    createUserKey = document.createUserKey,
                    createDt = document.createDt,
                    updateUserKey = document.updateUserKey,
                    updateDt = document.updateDt,
                    totalCount = queryResult.total
                )
            )
        }
        return documentList
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
            documentColor = document.documentColor,
            documentGroup = document.documentGroup
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
            WfTokenDto(elementId = wfElementService.getStartElement(documentEntity.process.processId).elementId)
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
            documentColor = restTemplateDocumentDto.documentColor,
            documentGroup = restTemplateDocumentDto.documentGroup
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)
        createDocumentDisplay(dataEntity) // 신청서 양식 정보 초기화
        updateFormAndProcessStatus(dataEntity)

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
            documentGroup = dataEntity.documentGroup
        )
    }

    /**
     * Update Document.
     *
     * @param restTemplateDocumentDto
     * @return Boolean
     */
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
        wfDocumentEntity.numberingRule =
            aliceNumberingRuleRepository.findById(restTemplateDocumentDto.documentNumberingRuleId).get()
        wfDocumentEntity.documentColor = restTemplateDocumentDto.documentColor
        wfDocumentEntity.documentGroup = restTemplateDocumentDto.documentGroup
        updateFormAndProcessStatus(wfDocumentRepository.save(wfDocumentEntity))

        if (params["isDeleteData"].toString().toBoolean()) {
            logger.debug("Delete Instance Data... (Document Id: {})", wfDocumentEntity.documentId)
            wfDocumentEntity.instance?.let { wfInstanceRepository.deleteInstances(it) }
        }

        return true
    }

    /**
     * Update Form and Process status.
     */
    fun updateFormAndProcessStatus(documentEntity: WfDocumentEntity) {
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
     * [processId]로 조회한 processEntity의 elementEntity 중 userTask 를 정렬하여 [List]로 반환
     */
    fun makeSortedUserTasks(processId: String): List<Map<String, Any>> {
        // userTask를 저장할 변수
        val sortedUserTasks: MutableList<Map<String, Any>> = mutableListOf()
        // 게이트웨이가 가지는 컨넥터(화살표)를 저장할 큐
        val arrowConnectorsQueueInGateway: MutableMap<String, ArrayDeque<WfElementEntity>> = mutableMapOf()
        // 모든 컨넥터(화살표)를 꺼내서 체크된 gateway id 저장할 변수
        val checkedGatewayIds: MutableMap<String, String> = mutableMapOf()
        // gateway에 여러개의 경우의 수가 있을 경우 저장
        val gatewayQueue = ArrayDeque<WfElementEntity>()
        val process = wfProcessRepository.getOne(processId)
        val processElementEntities = process.elementEntities

        // 쓸모 없는 그룹, 주석을 제거
        val allElementEntitiesInProcess: List<WfElementEntity> = processElementEntities.filter {
            WfElementConstants.ElementType.getAtomic(it.elementType) != WfElementConstants.ElementType.ARTIFACT
        }

        // 첫 commonStart 엘리먼트 찾기
        val startElement = allElementEntitiesInProcess.first {
            it.elementType == WfElementConstants.ElementType.COMMON_START_EVENT.value
        }
        var currentElement = startElement

        // while을 돌면서 전체 프로세스 확인
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
     * Search Document Display data.
     *
     * @param documentId
     * @return RestTemplateDocumentDisplayViewDto
     */
    fun getDocumentDisplay(documentId: String): RestTemplateDocumentDisplayViewDto {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val userTasks = makeSortedUserTasks(documentEntity.process.processId)
        val componentEntities =
            wfComponentRepository.findByFormIdAndComponentTypeNot(documentEntity.form.formId, "editbox")
        val displayList = wfDocumentDisplayRepository.findByDocumentId(documentId)

        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        for (component in componentEntities) {
            var isDisplay = false
            val displayValue: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
            for (elementEntity in userTasks) {
                for (display in displayList) {
                    if (display.componentId == component.componentId) {
                        if (display.elementId == elementEntity["elementId"].toString()) {
                            val displayMap = LinkedHashMap<String, Any>()
                            displayMap["elementId"] = display.elementId
                            displayMap["display"] = display.display
                            displayValue.add(displayMap)
                            isDisplay = true
                        }
                    }
                }
                // 강제적으로 compoent가 추가 되었을때 기본값 출력
                if (!isDisplay) {
                    val displayMap = LinkedHashMap<String, Any>()
                    displayMap["elementId"] = elementEntity["elementId"].toString()
                    displayMap["display"] = WfDocumentConstants.DisplayType.EDITABLE.value
                    displayValue.add(displayMap)
                    isDisplay = false
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
            elements = userTasks,
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

            // 컨넥터를 모두 꺼내서 사용 했으면 해당 gateway는 삭제하고 allCheckedGatewayIds 에 기록한다.
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
