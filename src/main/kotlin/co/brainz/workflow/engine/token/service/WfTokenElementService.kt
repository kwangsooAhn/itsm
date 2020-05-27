package co.brainz.workflow.engine.token.service

import co.brainz.framework.numbering.service.AliceNumberingService
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.element.constants.WfElementConstants
import co.brainz.workflow.engine.element.entity.WfElementDataEntity
import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.service.WfActionService
import co.brainz.workflow.engine.element.service.WfElementService
import co.brainz.workflow.engine.folder.service.WfFolderService
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.instance.service.WfInstanceService
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.entity.WfCandidateEntity
import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import co.brainz.workflow.engine.token.repository.WfCandidateRepository
import co.brainz.workflow.engine.token.repository.WfTokenDataRepository
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
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
    private val aliceNumberingService: AliceNumberingService,
    private val wfTokenMappingValue: WfTokenMappingValue,
    private val wfCandidateRepository: WfCandidateRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    lateinit var assigneeId: String

    /**
     * Init Token.
     *
     * @param restTemplateTokenDto
     */
    fun initToken(restTemplateTokenDto: RestTemplateTokenDto) {
        this.assigneeId = restTemplateTokenDto.assigneeId.toString()
        val documentDto =
            restTemplateTokenDto.documentId?.let { wfDocumentRepository.findDocumentEntityByDocumentId(it) }
        val documentNo =
            documentDto?.numberingRule?.numberingId?.let { aliceNumberingService.getNewNumbering(it) }.orEmpty()
        val instanceDto =
            documentDto?.let {
                RestTemplateInstanceDto(
                    instanceId = "",
                    document = it,
                    documentNo = documentNo,
                    pTokenId = restTemplateTokenDto.parentTokenId,
                    instanceCreateUser = restTemplateTokenDto.instanceCreateUser
                )
            }
        val instance = instanceDto?.let { wfInstanceService.createInstance(it) }
        instance?.let {
            val folders = wfFolderService.createFolder(instance)
            instance.folders = mutableListOf(folders)
            instance.pTokenId?.let { id ->
                val parentToken = wfTokenRepository.getOne(id)
                wfFolderService.createRelatedFolder(parentToken.instance, instance)
                wfFolderService.createRelatedFolder(instance, parentToken.instance)
            }
        }

        val wfDocumentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(restTemplateTokenDto.documentId!!)
        val startElement = wfElementService.getStartElement(wfDocumentEntity.process.processId)
        restTemplateTokenDto.elementType = startElement.elementType
        restTemplateTokenDto.elementId = startElement.elementId
        when (startElement.elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value -> {
                // Add commonStart token
                restTemplateTokenDto.tokenStatus = WfTokenConstants.Status.FINISH.code
                val commonStartToken = wfTokenActionService.createToken(instance!!, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = commonStartToken.tokenId
                // Add userTask token
                val arrows = wfActionService.getArrowElements(commonStartToken.element.elementId)
                val nextElementId = wfActionService.getNextElementId(arrows[0])
                val nextElement = wfActionService.getElement(nextElementId)
                when (nextElement.elementType) {
                    WfElementConstants.ElementType.USER_TASK.value -> {
                        // 최초 신청서 작성자 : restTemplateTokenDto.assigneeId (작성자)
                        // restTemplateTokenDto.assigneeId 값을 제거, 변경 하여 작성자 변경 가능
                        val userTaskToken =
                            setNextTokenSave(makeToken(nextElement, commonStartToken.instance), restTemplateTokenDto)
                        restTemplateTokenDto.tokenId = userTaskToken.tokenId
                        setCandidate(userTaskToken)
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
        this.assigneeId = restTemplateTokenDto.assigneeId.toString()
        val wfTokenEntity = wfTokenRepository.findTokenEntityByTokenId(restTemplateTokenDto.tokenId).get()
        val elementType = wfTokenEntity.element.elementType
        logger.debug("Token Element Type : {}", elementType)
        when (elementType) {
            WfElementConstants.ElementType.COMMON_START_EVENT.value ->
                setCommonAction(wfTokenEntity, restTemplateTokenDto)
            WfElementConstants.ElementType.USER_TASK.value ->
                setUserTaskAction(wfTokenEntity, restTemplateTokenDto)
            WfElementConstants.ElementType.MANUAL_TASK.value ->
                setManualTaskAction(wfTokenEntity, restTemplateTokenDto)
            WfElementConstants.ElementType.SIGNAL_SEND.value ->
                setSignalSend(wfTokenEntity, restTemplateTokenDto)
        }
    }

    /**
     * Action - ManualTask.
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     */
    private fun setManualTaskAction(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
        goToNext(wfTokenEntity, restTemplateTokenDto)
    }

    /**
     * Action common
     */
    private fun setCommonAction(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        logger.debug("Token Action : {}", restTemplateTokenDto.action)
        when (restTemplateTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> wfTokenActionService.save(wfTokenEntity, restTemplateTokenDto)
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
                goToNext(wfTokenEntity, restTemplateTokenDto)
            }
        }
    }

    /**
     * Action - UserTask.
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     */
    private fun setUserTaskAction(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        logger.debug("Token Action : {}", restTemplateTokenDto.action)
        when (restTemplateTokenDto.action) {
            WfElementConstants.Action.SAVE.value -> {
                wfTokenActionService.save(wfTokenEntity, restTemplateTokenDto)
            }
            WfElementConstants.Action.REJECT.value -> {
                val values = HashMap<String, Any>()
                values[WfElementConstants.AttributeId.REJECT_ID.value] = getAttributeValue(
                    wfTokenEntity.element.elementDataEntities,
                    WfElementConstants.AttributeId.REJECT_ID.value
                )
                wfTokenActionService.setReject(wfTokenEntity, restTemplateTokenDto, values)
            }
            WfElementConstants.Action.WITHDRAW.value -> wfTokenActionService.setWithdraw(
                wfTokenEntity,
                restTemplateTokenDto
            )
            else -> {
                wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
                goToNext(wfTokenEntity, restTemplateTokenDto)
            }
        }
    }

    /**
     * 시그널 이벤트
     */
    private fun setSignalSend(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        wfTokenActionService.setProcess(wfTokenEntity, restTemplateTokenDto)
        goToNext(wfTokenEntity, restTemplateTokenDto)
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
        saveTokenEntity.tokenData = wfTokenDataRepository.saveAll(dataList)

        return saveTokenEntity
    }

    private fun makeToken(element: WfElementEntity, instance: WfInstanceEntity): WfTokenEntity {
        val token = WfTokenEntity(
            tokenId = "",
            element = element,
            tokenStatus = WfTokenConstants.Status.RUNNING.code,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = instance
        )
        when (element.elementType) {
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                token.assigneeId = this.assigneeId
                token.tokenStatus = WfTokenConstants.Status.FINISH.code
                token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
            WfElementConstants.ElementType.COMMON_END_EVENT.value,
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value,
            WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                token.tokenStatus = WfTokenConstants.Status.FINISH.code
                token.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            }
        }

        return token
    }

    /**
     * Add Next Token. (+ TokenData)
     *
     * @param wfTokenEntity
     * @param restTemplateTokenDto
     */
    private fun goToNext(
        wfTokenEntity: WfTokenEntity,
        restTemplateTokenDto: RestTemplateTokenDto
    ) {
        restTemplateTokenDto.elementId = wfTokenEntity.element.elementId
        val nextElementEntity = wfElementService.getNextElement(restTemplateTokenDto)

        when (nextElementEntity.elementType) {
            WfElementConstants.ElementType.COMMON_END_EVENT.value -> {
                val token = makeToken(nextElementEntity, wfTokenEntity.instance)
                wfTokenRepository.save(token)
                wfInstanceService.completeInstance(wfTokenEntity.instance.instanceId)

                // 서브프로세스 토큰 불러오기
                if (!wfTokenEntity.instance.pTokenId.isNullOrEmpty()) {
                    val pTokenId = wfTokenEntity.instance.pTokenId!!
                    val mainProcessToken = wfTokenRepository.findTokenEntityByTokenId(pTokenId).get()
                    mainProcessToken.tokenStatus = WfTokenConstants.Status.FINISH.code
                    mainProcessToken.tokenEndDt = LocalDateTime.now(ZoneId.of("UTC"))

                    restTemplateTokenDto.tokenId = mainProcessToken.tokenId
                    restTemplateTokenDto.data = wfTokenMappingValue.makeSubProcessTokenDataDto(
                        wfTokenEntity,
                        mainProcessToken
                    )
                    setNextTokenSave(mainProcessToken, restTemplateTokenDto)
                    goToNext(mainProcessToken, restTemplateTokenDto)
                }
            }
            WfElementConstants.ElementType.USER_TASK.value -> {
                val token = makeToken(nextElementEntity, wfTokenEntity.instance)
                val saveToken = setNextTokenSave(token, restTemplateTokenDto)
                setCandidate(saveToken)
            }
            WfElementConstants.ElementType.MANUAL_TASK.value -> {
                val token = makeToken(nextElementEntity, wfTokenEntity.instance)
                val saveToken = setNextTokenSave(token, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveToken.tokenId
                goToNext(saveToken, restTemplateTokenDto)
            }
            WfElementConstants.ElementType.EXCLUSIVE_GATEWAY.value -> {
                val token = makeToken(nextElementEntity, wfTokenEntity.instance)
                val saveToken = setNextTokenSave(token, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveToken.tokenId
                goToNext(saveToken, restTemplateTokenDto)
            }
            WfElementConstants.ElementType.SUB_PROCESS.value -> {
                // nextElementEntity 는 현재 실행해야할 task의 엔티티다.
                val token = makeToken(nextElementEntity, wfTokenEntity.instance)
                val saveToken = setNextTokenSave(token, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveToken.tokenId

                // sub-document-id 확인 후 신규 인스턴스와 토큰을 생성
                val documentId = getAttributeValue(
                    nextElementEntity.elementDataEntities,
                    WfElementConstants.AttributeId.SUB_DOCUMENT_ID.value
                )
                val makeDocumentTokens =
                    wfTokenMappingValue.makeRestTemplateTokenDto(saveToken, mutableListOf(documentId))
                makeDocumentTokens.forEach {
                    it.instanceCreateUser = restTemplateTokenDto.instanceCreateUser
                    initToken(it)
                }
            }
            WfElementConstants.ElementType.SIGNAL_SEND.value -> {
                val token = makeToken(nextElementEntity, wfTokenEntity.instance)
                val saveToken = setNextTokenSave(token, restTemplateTokenDto)
                restTemplateTokenDto.tokenId = saveToken.tokenId

                // target-document-list 확인 후 신규 인스턴스와 토큰을 생성
                val targetDocumentIds = mutableListOf<String>()
                token.element.elementDataEntities.forEach {
                    if (it.attributeId == WfElementConstants.AttributeId.TARGET_DOCUMENT_LIST.value) {
                        targetDocumentIds.add(it.attributeValue)
                    }
                }
                val makeDocumentTokens =
                    wfTokenMappingValue.makeRestTemplateTokenDto(saveToken, targetDocumentIds)
                makeDocumentTokens.forEach {
                    it.instanceCreateUser = restTemplateTokenDto.instanceCreateUser
                    initToken(it)
                }
                goToNext(saveToken, restTemplateTokenDto)
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
     * Get AttributeValues.
     *
     * @param elementDataEntities
     * @param attributeId
     * @return MutableList<String> (attributeValue)
     */
    private fun getAttributeValues(
        elementDataEntities: MutableList<WfElementDataEntity>,
        attributeId: String
    ): MutableList<String> {
        val attributeValues: MutableList<String> = mutableListOf()
        elementDataEntities.forEach { data ->
            if (data.attributeId == attributeId) {
                attributeValues.add(data.attributeValue)
            }
        }
        return attributeValues
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
            if (component.mappingId.isNotEmpty() && component.mappingId == assigneeMappingId) {
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
     * Set token assignee or candidate.
     *
     * @param token
     */
    private fun setCandidate(token: WfTokenEntity) {
        val assigneeType =
            getAttributeValue(token.element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE_TYPE.value)
        when (assigneeType) {
            WfTokenConstants.AssigneeType.ASSIGNEE.code -> {
                var assigneeId = getAssignee(token.element, token)
                if (assigneeId.isEmpty()) {
                    assigneeId = this.assigneeId
                }
                token.assigneeId = assigneeId
                wfTokenRepository.save(token)
            }
            WfTokenConstants.AssigneeType.USERS.code,
            WfTokenConstants.AssigneeType.GROUPS.code -> {
                val candidates =
                    getAttributeValues(token.element.elementDataEntities, WfElementConstants.AttributeId.ASSIGNEE.value)
                if (candidates.isNotEmpty()) {
                    val wfCandidateEntities = mutableListOf<WfCandidateEntity>()
                    candidates.forEach { candidate ->
                        val wfCandidateEntity = WfCandidateEntity(
                            token = token,
                            candidateType = assigneeType,
                            candidateValue = candidate
                        )
                        wfCandidateEntities.add(wfCandidateEntity)
                    }
                    wfCandidateRepository.saveAll(wfCandidateEntities)
                } else {
                    token.assigneeId = this.assigneeId
                    wfTokenRepository.save(token)
                }
            }
        }
    }
}
