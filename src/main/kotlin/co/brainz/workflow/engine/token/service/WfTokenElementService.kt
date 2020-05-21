package co.brainz.workflow.engine.token.service

import co.brainz.framework.numbering.service.AliceNumberingService
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.folder.service.WfFolderService
import co.brainz.workflow.engine.instance.constants.WfInstanceConstants
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import java.time.LocalDateTime
import java.time.ZoneId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfTokenElementService(
    private val wfTokenActionService: WfTokenActionService,
    private val wfActionService: WfActionService,
    private val wfTokenRepository: WfTokenRepository,
    private val wfInstanceService: WfInstanceService,
    private val wfElementService: WfElementService,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val wfFolderService: WfFolderService,
    private val aliceNumberingService: AliceNumberingService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * Init Token.
     *
     * @param restTemplateTokenDto
     */
    fun initToken(restTemplateTokenDto: RestTemplateTokenDto) {
        val documentDto =
            restTemplateTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val documentNo =
            documentDto?.numberingRule?.numberingId?.let { aliceNumberingService.getNewNumbering(it) }.orEmpty()
        val instanceDto =
            documentDto?.let { RestTemplateInstanceDto(instanceId = "", document = it, documentNo = documentNo) }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }
        instance?.let { wfFolderService.createFolder(instance) }

        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(restTemplateTokenDto.documentId!!)
        val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
        restTemplateTokenDto.elementType = startElement.elementType
        restTemplateTokenDto.elementId = startElement.elementId
        when (startElement.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                //Add commonStart token
                restTemplateTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
                val commonStartToken = wfTokenActionService.createToken(instance!!, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = commonStartToken.tokenId
                //Add userTask token
                val arrows = wfActionService.getArrowElements(commonStartToken.element.elementId)
                val nextElementId = wfActionService.getNextElementId(arrows[0])
                val nextElement = wfActionService.getElement(nextElementId)
                when (nextElement.elementType) {
                    WfElementConstants.ElementType.USER_TASK.value -> {
                        val userTaskToken = setNextTokenSave(
                            WfTokenEntity(
                                tokenId = "",
                                element = nextElement,
                                tokenStatus = WfTokenConstants.Status.RUNNING.code,
                                tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
                                instance = commonStartToken.instance
                            ), restTemplateTokenDto
                        )
                        restTemplateTokenDto.tokenId = userTaskToken.tokenId

                        //Set userTask assginee
                        when (getAttributeValue(
                            nextElement.elementDataEntities,
                            WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
                        )) {
                            WfTokenConstants.AssigneeType.ASSIGNEE.code -> {
                                userTaskToken.assigneeId = getAssignee(nextElement, userTaskToken)
                            }
                            WfTokenConstants.AssigneeType.USERS.code -> {
                                userTaskToken.assigneeId = getAssigneeUser(nextElement)
                            }
                            WfTokenConstants.AssigneeType.GROUPS.code -> {
                                // TODO: 담당자 그룹에 따른 처리
                            }
                        }
                        wfTokenActionService.save(userTaskToken, restTemplateTokenDto)
                    }
                }
            }
        }
        setTokenAction(restTemplateTokenDto)
    }

    /**
     * Token Gate (Action).
     *
     * @param restTemplateTokenDto
     */
    fun setTokenAction(restTemplateTokenDto: RestTemplateTokenDto) {
        val wfTokenEntity = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
        val wfElementEntity = wfActionService.getElement(wfTokenEntity.element.elementId)
        logger.debug("Token Element Type : {}", wfElementEntity.elementType)
        when (wfElementEntity.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> setCommonAction(
                wfTokenEntity,
                wfElementEntity,
                restTemplateTokenDto
            )
            WfElementConstants.ElementType.USER_TASK.value -> setUserTaskAction(
                wfTokenEntity,
                wfElementEntity,
                restTemplateTokenDto
            )
            WfElementConstants.ElementType.MANUAL_TASK.value -> setManualTaskAction(
                wfTokenEntity,
                wfElementEntity,
                restTemplateTokenDto
            )
            WfElementConstants.ElementType.SIGNAL_SEND.value -> setSignalSend(
                wfTokenEntity,
                wfElementEntity,
                restTemplateTokenDto
            )
        }
    }

    /**
     * Action - ManualTask.
     *
     * @param wfTokenEntity
     * @param wfElementEntity
     * @param restTemplateTokenDto
     */
    private fun setManualTaskAction(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
        goToNext(wfTokenEntity, wfElementEntity, restTemplateTokenDto)
    }

    /**
     * Action common
     */
    private fun setCommonAction(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        logger.debug("Token Action : {}", restTemplateTokenDto.action)
        when (restTemplateTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, restTemplateTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
                goToNext(wfTokenEntity, wfElementEntity, restTemplateTokenDto)
            }
        }
    }

    /**
     * Action - UserTask.
     *
     * @param wfTokenEntity
     * @param wfElementEntity
     * @param restTemplateTokenDto
     */
    private fun setUserTaskAction(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        logger.debug("Token Action : {}", restTemplateTokenDto.action)
        when (restTemplateTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> {
                restTemplateTokenDto.assigneeId = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
                wfTokenActionService.save(wfTokenEntity, restTemplateTokenDto)
            }
            WfElementConstants.Action.REJECT.value -> {
                val values = HashMap<String, Any>()
                values[WfElementConstants.AttributeId.REJECT_ID.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.REJECT_ID.value
                )
                values[WfElementConstants.AttributeId.ASSIGNEE_TYPE.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
                )
                values[WfElementConstants.AttributeId.ASSIGNEE.value] = getAttributeValue(
                    wfElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE.value
                )
                wfTokenActionService.setReject(wfTokenEntity, restTemplateTokenDto, values)
            }
            WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.setWithdraw(
                wfTokenEntity,
                restTemplateTokenDto
            )
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
                goToNext(wfTokenEntity, wfElementEntity, restTemplateTokenDto)
            }
        }
    }

    /**
     * 시그널 이벤트
     */
    private fun setSignalSend(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
        goToNext(wfTokenEntity, wfElementEntity, restTemplateTokenDto)
    }

    /**
     * Set Next Token Save.
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     * @return WfTokenEntity
     */
    private fun setNextTokenSave(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ): WfTokenEntity {
        val saveTokenEntity = wfTokenRepository.save(wfTokenEntity)

        // Token Data
        val dataList: MutableList<WfTokenDataEntity> = mutableListOf()
        restTemplateTokenDto.data?.forEach {
            dataList.add(
                WfTokenDataEntity(
                    tokenId = saveTokenEntity.tokenId,
                    componentId = it.componentId,
                    value = it.value
                )
            )
        }
        wfTokenDataRepository.saveAll(dataList)

        return saveTokenEntity
    }

    /**
     * Set Next Token Entity.
     *
     * @param nextElementEntity
     * @param wfTokenEntity
     * @return WfTokenEntity
     */
    private fun setNextTokenEntity(nextElementEntity: WfElementEntity, wfTokenEntity: WfTokenEntity): WfTokenEntity {
        val nextTokenEntity = WfTokenEntity(
            tokenId = "",
            element = nextElementEntity,
            tokenStatus = WfTokenConstants.Status.RUNNING.code,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfTokenEntity.instance
        )
        when (nextElementEntity.elementType) {
            WfElementConstants.ElementType.USER_TASK.value -> {
                when (getAttributeValue(
                    nextElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.ASSIGNEE_TYPE.value
                )) {
                    WfTokenConstants.AssigneeType.ASSIGNEE.code -> {
                        nextTokenEntity.assigneeId = getAssignee(nextElementEntity, wfTokenEntity)
                    }
                    WfTokenConstants.AssigneeType.USERS.code -> {
                        nextTokenEntity.assigneeId = getAssigneeUser(nextElementEntity)
                    }
                    WfTokenConstants.AssigneeType.GROUPS.code -> {
                        // TODO: 담당자 그룹에 따른 처리
                    }
                }
            }
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
                nextTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                nextTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
                nextTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                nextTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value, WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                nextTokenEntity.assigneeId = wfTokenEntity.assigneeId
                nextTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                nextTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
        }
        return nextTokenEntity
    }

    /**
     * Add Next Token. (+ TokenData)
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     */
    private fun goToNext(
        wfTokenEntity: WfTokenEntity,
        wfElementEntity: WfElementEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        restTemplateTokenDto.elementId = wfElementEntity.elementId
        val nextElementEntity = wfElementService.getNextElement(restTemplateTokenDto)

        when (nextElementEntity.elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                wfTokenRepository.save(newTokenEntity)
                wfInstanceService.completeInstance(wfTokenEntity.instance.instanceId)
                if (!wfTokenEntity.instance.pTokenId.isNullOrEmpty()) {
                    val pTokenId = wfTokenEntity.instance.pTokenId!!
                    val pTokenEntity = wfTokenRepository.findTokenEntityByTokenId(pTokenId).get()
                    pTokenEntity.tokenStatus = WfTokenConstants.Status.FINISH.code
                    pTokenEntity.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
                    val savePTokenEntity = wfTokenRepository.save(pTokenEntity)
                    val newElementEntity = wfActionService.getElement(savePTokenEntity.element.elementId)
                    // TODO: 문서의 양식이 다르기 때문에 데이터가 다르다. wfTokenDto 값을 현재 문서에 맞게 갱신하는 작업 필요 (mapping-id)
                    goToNext(savePTokenEntity, newElementEntity, restTemplateTokenDto)
                }
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                setNextTokenSave(newTokenEntity, restTemplateTokenDto)
            }
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                val saveTokenEntity = setNextTokenSave(newTokenEntity, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveTokenEntity.tokenId
                val newElementEntity = wfActionService.getElement(saveTokenEntity.element.elementId)
                goToNext(saveTokenEntity, newElementEntity, restTemplateTokenDto)
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                val saveTokenEntity = setNextTokenSave(newTokenEntity, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveTokenEntity.tokenId
                val newElementEntity = wfActionService.getElement(saveTokenEntity.element.elementId)
                goToNext(saveTokenEntity, newElementEntity, restTemplateTokenDto)
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                // TODO: SubProcess 로 시작시 초기 데이터를 갱신해야한다. 데이터 구조가 다름. (wfTokenDto 를 mapping-id로 처리)
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                val saveTokenEntity = setNextTokenSave(newTokenEntity, restTemplateTokenDto)

                // New Instance
                val documentId = getAttributeValue(
                    nextElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
                )
                val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
                val wfInstanceDto = RestTemplateInstanceDto(
                    instanceId = "",
                    document = wfDocumentEntity,
                    instanceStatus = WfInstanceConstants.Status.RUNNING.code,
                    pTokenId = saveTokenEntity.tokenId,
                    documentNo = aliceNumberingService.getNewNumbering(wfDocumentEntity.numberingRule.numberingId)
                )
                val wfInstanceEntity = wfInstanceService.createInstance(wfInstanceDto)
                wfFolderService.addInstance(originInstance = wfTokenEntity.instance, addedInstance = wfInstanceEntity)

                // Call Document Start Element
                val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
                restTemplateTokenDto.elementType = startElement.elementType
                restTemplateTokenDto.elementId = startElement.elementId
                when (startElement.elementType) {
                    WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                        restTemplateTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
                    }
                }
                val startToken = wfTokenActionService.createToken(wfInstanceEntity, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = startToken.tokenId
                goToNext(startToken, startElement, restTemplateTokenDto)
            }
            WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                val newTokenEntity = setNextTokenEntity(nextElementEntity, wfTokenEntity)
                val saveTokenEntity = setNextTokenSave(newTokenEntity, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveTokenEntity.tokenId
                val newElementEntity = wfActionService.getElement(saveTokenEntity.element.elementId)
                val tokenId = restTemplateTokenDto.tokenId

                // 종료된 토큰의 WfTokenDto.elementId 로 WfElementDataEntity 조회 후 target-document-list 확인
                val targetDocumentIds = mutableListOf<String>()
                nextElementEntity.elementDataEntities.forEach {
                    if (it.attributeId == WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) {
                        targetDocumentIds.add(it.attributeValue)
                    }
                }

                // 종료된 토큰의 데이터(tokenData)를 확인하여 컴포넌트ID를 모두 찾는다.
                val tokenData = wfTokenDataRepository.findTokenDataEntityByTokenId(tokenId)
                val componentIdInTokenData = tokenData.map { it.componentId }

                // 종료된 토큰의 componentId 별로 mappingId를 찾는다.
                val token = wfTokenRepository.getOne(tokenId)
                val component = token.instance.document!!.form.components!!.filter {
                    componentIdInTokenData.contains(it.componentId) && it.mappingId.isNotBlank()
                }
                val mappingIds = component.associateBy({ it.componentId }, { it.mappingId })

                // mappingId 별로 실제 토큰에 저장된 value를 찾아 복제할 데이터를 생성한다.
                val tokenDataForCopy = mutableMapOf<String, String>()
                tokenData.forEach {
                    if (mappingIds[it.componentId] != null) {
                        val mappingId = mappingIds[it.componentId] as String
                        tokenDataForCopy[mappingId] = it.value
                    }
                }

                // target-document-list 별로 수행해야할 토큰을 리턴
                targetDocumentIds.forEach { documentId ->
                    val document = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)

                    // 복제할 데이터에서 타켓 다큐먼트의 mappingId 와 일치하는 value를 찾고
                    // WfTokenDataDto 를 생성한다.
                    val tokenDataList = mutableListOf<RestTemplateTokenDataDto>()
                    document.form.components!!.forEach { component ->
                        if (component.mappingId.isNotBlank() && tokenDataForCopy[component.mappingId] != null) {
                            val value = tokenDataForCopy[component.mappingId] as String
                            val data = RestTemplateTokenDataDto(componentId = component.componentId, value = value)
                            tokenDataList.add(data)
                        }
                    }

                    // RestTemplateTokenDto 생성 후 토큰 실행!!
                    initToken(RestTemplateTokenDto(documentId = document.documentId, data = tokenDataList))
                }
                goToNext(saveTokenEntity, newElementEntity, restTemplateTokenDto)
            }
        }
    }

    /**
     * Get AttributeValue.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return String (attributeValue)
     */
    private fun getAttributeValue(elementDataEntities: MutableList<WfElementDataEntity>, attributeId: String): String {
        var attributeValue = ""
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValue = data.attributeValue
            }
        }
        return attributeValue
    }

    /**
     * Get Assignee.
     *
     * @param element
     * @param token
     * @return String
     */
    private fun getAssignee(element: WfElementEntity, token: WfTokenEntity): String {
        val assigneeMappingId =
            getAttributeValue(element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
        var componentMappingId = ""
        token.instance.document!!.form.components?.forEach { component ->
            if (component.mappingId == assigneeMappingId) {
                componentMappingId = component.componentId
            }
        }
        var assignee = ""
        if (componentMappingId.isNotEmpty()) {
            assignee =
                wfTokenDataRepository.findByTokenIdAndComponentId(token.tokenId, componentMappingId).value.split("|")[0]
        }
        return assignee
    }

    /**
     * Get AssigneeUser.
     *
     * @param element
     * @return String
     */
    private fun getAssigneeUser(element: WfElementEntity): String {
        return getAttributeValue(
            element.elementDataEntities,
            WfElementConstants.AttributeId.ASSIGNEE.value
        ).split(",")[0]
    }
}
