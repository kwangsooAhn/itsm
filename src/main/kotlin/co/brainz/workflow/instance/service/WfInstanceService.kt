package co.brainz.workflow.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.numbering.service.AliceNumberingService
import co.brainz.workflow.comment.service.WfCommentService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.mapper.WfTokenMapper
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.folder.service.WfFolderService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import java.time.ZoneId
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfInstanceService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val aliceUserRoleMapRepository: AliceUserRoleMapRepository,
    private val wfCommentService: WfCommentService,
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceNumberingService: AliceNumberingService,
    private val aliceUserRepository: AliceUserRepository,
    private val wfFolderService: WfFolderService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val wfTokenMapper: WfTokenMapper = Mappers.getMapper(WfTokenMapper::class.java)

    /**
     * Search Instances.
     */
    fun instances(parameters: LinkedHashMap<String, Any>): List<RestTemplateInstanceViewDto> {
        var status = ""
        var userKey = ""

        if (parameters["status"] != null) {
            status = parameters["status"].toString()
        }
        if (parameters["userKey"] != null) {
            userKey = parameters["userKey"].toString()
        }

        val tokens = mutableListOf<RestTemplateInstanceViewDto>()
        val instances: MutableList<WfInstanceListViewDto> = mutableListOf()
        val roleEntities = aliceUserRoleMapRepository.findUserRoleByUserKey(userKey)
        val runningInstances = wfInstanceRepository.findInstances(status)
        runningInstances.forEach { instance ->
            if (instance.tokenEntity.tokenStatus == WfTokenConstants.Status.RUNNING.code) {
                if (instance.tokenEntity.assigneeId == userKey) {
                    instances.add(
                        WfInstanceListViewDto(
                            documentEntity = instance.documentEntity,
                            instanceEntity = instance.instanceEntity,
                            tokenEntity = instance.tokenEntity
                        )
                    )
                }
                instance.tokenEntity.candidate?.forEach { candidate ->
                    when (candidate.candidateType) {
                        WfTokenConstants.AssigneeType.USERS.code -> {
                            if (candidate.candidateValue == userKey) {
                                instances.add(
                                    WfInstanceListViewDto(
                                        documentEntity = instance.documentEntity,
                                        instanceEntity = instance.instanceEntity,
                                        tokenEntity = instance.tokenEntity
                                    )
                                )
                            }
                        }
                        WfTokenConstants.AssigneeType.GROUPS.code -> {
                            roleEntities.forEach roleForEach@{ role ->
                                if (role.roleId == candidate.candidateValue) {
                                    instances.add(
                                        WfInstanceListViewDto(
                                            documentEntity = instance.documentEntity,
                                            instanceEntity = instance.instanceEntity,
                                            tokenEntity = instance.tokenEntity
                                        )
                                    )
                                    return@roleForEach
                                }
                            }
                        }
                    }
                }
            }
        }

        val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
        for (instance in instances) {

            val topics: MutableList<String> = mutableListOf()

            // 문서 별로 목록에 출력하는 topic 컴포넌트 리스트를 구함.
            val topicComponentList =
                wfComponentRepository.findTopicComponentForDisplay(
                    instance.documentEntity.form.formId,
                    true,
                    componentTypeForTopicDisplay
                )

            // topic 컴포넌트의 실제 값들을 조회.
            for (topicComponent in topicComponentList) {
                topics.add(
                    wfTokenDataRepository.findByTokenIdAndComponentId(
                        instance.tokenEntity.tokenId,
                        topicComponent.componentId
                    ).value
                )
            }

            tokens.add(
                RestTemplateInstanceViewDto(
                    tokenId = instance.tokenEntity.tokenId,
                    instanceId = instance.instanceEntity.instanceId,
                    documentName = instance.documentEntity.documentName,
                    documentDesc = instance.documentEntity.documentDesc,
                    topics = topics,
                    createDt = instance.instanceEntity.instanceStartDt,
                    assigneeUserKey = instance.tokenEntity.assigneeId,
                    assigneeUserName = "",
                    createUserKey = instance.instanceEntity.instanceCreateUser?.userKey,
                    createUserName = instance.instanceEntity.instanceCreateUser?.userName,
                    documentId = instance.documentEntity.documentId,
                    documentNo = instance.instanceEntity.documentNo,
                    documentColor = instance.documentEntity.documentColor
                )
            )
        }

        return tokens
    }

    /**
     * 인스턴스ID [instanceId] 로 인스턴스 정보를 조회한다.
     *
     */
    fun instance(instanceId: String): RestTemplateInstanceViewDto {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.convertValue(
            wfInstanceRepository.findByInstanceId(instanceId),
            RestTemplateInstanceViewDto::class.java
        )
    }

    /**
     * Instance Create.
     */
    fun createInstance(restTemplateTokenDto: RestTemplateTokenDto): WfInstanceEntity {
        val document = wfDocumentRepository.findDocumentEntityByDocumentId(restTemplateTokenDto.documentId)
        val documentNo = aliceNumberingService.getNewNumbering(document.numberingRule.numberingId)
        val user = restTemplateTokenDto.assigneeId?.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        val instanceEntity = WfInstanceEntity(
            instanceId = "",
            documentNo = documentNo,
            instanceStatus = RestTemplateConstants.InstanceStatus.RUNNING.value,
            instanceStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instanceCreateUser = user,
            pTokenId = restTemplateTokenDto.parentTokenId,
            document = document
        )
        val instance = wfInstanceRepository.save(instanceEntity)
        instance.let {
            val folders = wfFolderService.createFolder(instance)
            instance.folders = mutableListOf(folders)
            instance.pTokenId?.let { id ->
                val parentToken = wfTokenRepository.getOne(id)
                wfFolderService.createRelatedFolder(parentToken.instance, instance)
                wfFolderService.createRelatedFolder(instance, parentToken.instance)
            }
        }

        return instance
    }

    fun createInstance(restTemplateInstanceDto: RestTemplateInstanceDto): WfInstanceEntity {
        val instanceEntity = WfInstanceEntity(
            instanceId = "",
            instanceStatus = restTemplateInstanceDto.instanceStatus
                ?: WfInstanceConstants.Status.RUNNING.code,
            document = restTemplateInstanceDto.document,
            instanceStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        if (restTemplateInstanceDto.pTokenId != null) {
            instanceEntity.pTokenId = restTemplateInstanceDto.pTokenId
        }
        instanceEntity.documentNo = restTemplateInstanceDto.documentNo

        return wfInstanceRepository.save(instanceEntity)
    }

    /**
     * Instance Complete.
     */
    fun completeInstance(instanceId: String) {
        wfInstanceRepository.findByInstanceId(instanceId)?.let {
            it.instanceStatus = WfInstanceConstants.Status.FINISH.code
            it.instanceEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            wfInstanceRepository.save(it)
        }
    }

    /**
     * Instance Status Count
     */
    fun instancesStatusCount(parameters: LinkedHashMap<String, Any>): List<RestTemplateInstanceCountDto> {
        var userKey = ""
        if (parameters["userKey"] != null) {
            userKey = parameters["userKey"].toString()
        }
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val instanceStatusCountList = wfInstanceRepository.findInstancesCount(userKey)
        val counts = mutableListOf<RestTemplateInstanceCountDto>()
        for (instanceStatusCount in instanceStatusCountList) {
            counts.add(mapper.convertValue(instanceStatusCount, RestTemplateInstanceCountDto::class.java))
        }

        return counts
    }

    /**
     * Instance history
     */
    fun getInstancesHistory(instanceId: String): List<RestTemplateInstanceHistoryDto> {
        return wfInstanceRepository.findInstanceHistory(instanceId)
    }

    /**
     * 인스턴스ID[instanceId]로 마지막 토큰 정보를 조회한다.
     */
    fun getInstanceLatestToken(instanceId: String): RestTemplateTokenDto {
        var tokenDto = RestTemplateTokenDto()
        wfInstanceRepository.findByInstanceId(instanceId)?.let { instance ->
            wfTokenRepository.findTopByInstanceAndTokenStatusOrderByTokenStartDtDesc(instance)?.let { token ->
                tokenDto = wfTokenMapper.toTokenDto(token)
                val tokenDataList = mutableListOf<RestTemplateTokenDataDto>()
                token.tokenData?.forEach { tokenData ->
                    tokenDataList.add(wfTokenMapper.toTokenDataDto(tokenData))
                }
                tokenDto.data = tokenDataList
            }
        }

        logger.debug("Latest token: {}", tokenDto)
        return tokenDto
    }

    /**
     * Get Instance Comments.
     */
    fun getInstanceComments(instanceId: String): MutableList<RestTemplateCommentDto> {
        return wfCommentService.getInstanceComments(instanceId)
    }

    /**
     * Get Instance List
     */
    fun getAllInstanceListAndSearch(instanceId: String, searchValue: String): MutableList<RestTemplateInstanceListDto> {
        return wfInstanceRepository.findAllInstanceListAndSearch(instanceId, searchValue)
    }
}
