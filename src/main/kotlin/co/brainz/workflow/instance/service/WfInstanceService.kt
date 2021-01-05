package co.brainz.workflow.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import co.brainz.workflow.comment.service.WfCommentService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.folder.service.WfFolderService
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.dto.WfInstanceListTagDto
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.tag.repository.WfTagRepository
import co.brainz.workflow.tag.service.WfTagService
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.mapper.WfTokenMapper
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class WfInstanceService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfCommentService: WfCommentService,
    private val wfDocumentRepository: WfDocumentRepository,
    private val numberingRuleService: NumberingRuleService,
    private val aliceUserRepository: AliceUserRepository,
    private val wfFolderService: WfFolderService,
    private val wfTagRepository: WfTagRepository,
    private val wfTagService: WfTagService,
    private val userDetailsService: AliceUserDetailsService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val wfTokenMapper: WfTokenMapper = Mappers.getMapper(WfTokenMapper::class.java)

    /**
     * Search Instances.
     */
    fun instances(parameters: LinkedHashMap<String, Any>): List<RestTemplateInstanceViewDto> {

        var tagList = emptyList<String>()
        if (parameters["tags"].toString().isNotEmpty()) {
            tagList = StringUtils.trimAllWhitespace(parameters["tags"].toString())
                .replace("#", "")
                .split(",")
        }
        parameters["tags"] = HashSet<String>(tagList)

        val queryResults = when (parameters["tokenType"].toString()) {
            WfTokenConstants.SearchType.REQUESTED.code -> requestedInstances(parameters)
            WfTokenConstants.SearchType.PROGRESS.code -> relatedInstances(
                WfInstanceConstants.getTargetStatusGroup(
                    WfTokenConstants.SearchType.PROGRESS
                ), parameters
            )
            WfTokenConstants.SearchType.COMPLETED.code -> relatedInstances(
                WfInstanceConstants.getTargetStatusGroup(
                    WfTokenConstants.SearchType.COMPLETED
                ), parameters
            )
            else -> todoInstances(
                WfInstanceConstants.getTargetStatusGroup(
                    WfTokenConstants.SearchType.TODO
                ),
                WfTokenConstants.getTargetTokenStatusGroup(
                    WfTokenConstants.SearchType.TODO
                ), parameters
            )
        }

        val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
        val tokens = mutableListOf<RestTemplateInstanceViewDto>()

        // Topic
        val tokenIds = mutableSetOf<String>()
        val tokenDataList = mutableListOf<WfInstanceListTokenDataDto>()
        for (instance in queryResults.results) {
            tokenIds.add(instance.tokenEntity.tokenId)
        }
        if (tokenIds.isNotEmpty()) {
            tokenDataList.addAll(wfTokenDataRepository.findTokenDataByTokenIds(tokenIds))
        }

        // Tag
        val instanceIds = mutableSetOf<String>()
        val tagDataList = mutableListOf<WfInstanceListTagDto>()
        queryResults.results.forEach { result -> instanceIds.add(result.instanceEntity.instanceId) }
        if (instanceIds.isNotEmpty()) {
            tagDataList.addAll(wfTagRepository.findByInstanceIds(instanceIds))
        }

        for (instance in queryResults.results) {
            val topics = mutableListOf<String>()
            val topicComponentIds = mutableListOf<String>()
            tokenDataList.forEach { tokenData ->
                if (tokenData.component.isTopic &&
                    componentTypeForTopicDisplay.indexOf(tokenData.component.componentType) > -1
                ) {
                    if (instance.tokenEntity.tokenId == tokenData.component.tokenId) {
                        topicComponentIds.add(tokenData.component.componentId)
                        topics.add(tokenData.value)
                    }
                }
            }

            val tags = mutableListOf<String>()
            tagDataList.forEach { tagData ->
                if (tagData.instanceId == instance.instanceEntity.instanceId) {
                    tags.add(tagData.tagContent)
                }
            }

            val avatarPath = instance.instanceEntity.instanceCreateUser?.let {
                userDetailsService.makeAvatarPath(it)
            }

            tokens.add(
                RestTemplateInstanceViewDto(
                    tokenId = instance.tokenEntity.tokenId,
                    elementName = instance.tokenEntity.element.elementName,
                    instanceId = instance.instanceEntity.instanceId,
                    documentName = instance.documentEntity.documentName,
                    documentDesc = instance.documentEntity.documentDesc,
                    topics = topics,
                    tags = tags,
                    createDt = instance.instanceEntity.instanceStartDt,
                    assigneeUserKey = instance.tokenEntity.assigneeId,
                    assigneeUserName = "",
                    createUserKey = instance.instanceEntity.instanceCreateUser?.userKey,
                    createUserName = instance.instanceEntity.instanceCreateUser?.userName,
                    documentId = instance.documentEntity.documentId,
                    documentNo = instance.instanceEntity.documentNo,
                    documentColor = instance.documentEntity.documentColor,
                    avatarPath = avatarPath,
                    totalCount = queryResults.total
                )
            )
        }

        return tokens
    }

    /**
     * 신청한 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun requestedInstances(parameters: LinkedHashMap<String, Any>): QueryResults<WfInstanceListViewDto> {
        return wfInstanceRepository.findRequestedInstances(
            parameters["userKey"].toString(),
            parameters["documentId"].toString(),
            parameters["searchValue"].toString(),
            parameters["tags"] as Set<String>,
            LocalDateTime.parse(parameters["fromDt"].toString(), DateTimeFormatter.ISO_DATE_TIME),
            LocalDateTime.parse(parameters["toDt"].toString(), DateTimeFormatter.ISO_DATE_TIME).plusDays(1),
            parameters["offset"].toString().toLong()
        )
    }

    /**
     * 진행중 / 완료된 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun relatedInstances(
        status: List<String>?,
        parameters: LinkedHashMap<String, Any>
    ): QueryResults<WfInstanceListViewDto> {
        return wfInstanceRepository.findRelationInstances(
            status,
            parameters["userKey"].toString(),
            parameters["documentId"].toString(),
            parameters["searchValue"].toString(),
            parameters["tags"] as Set<String>,
            LocalDateTime.parse(parameters["fromDt"].toString(), DateTimeFormatter.ISO_DATE_TIME),
            LocalDateTime.parse(parameters["toDt"].toString(), DateTimeFormatter.ISO_DATE_TIME).plusDays(1),
            parameters["offset"].toString().toLong()
        )
    }

    /**
     * 처리할 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun todoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        parameters: LinkedHashMap<String, Any>
    ): QueryResults<WfInstanceListViewDto> {
        return wfInstanceRepository.findTodoInstances(
            status,
            tokenStatus,
            parameters["userKey"].toString(),
            parameters["documentId"].toString(),
            parameters["searchValue"].toString(),
            parameters["tags"] as Set<String>,
            LocalDateTime.parse(parameters["fromDt"].toString(), DateTimeFormatter.ISO_DATE_TIME),
            LocalDateTime.parse(parameters["toDt"].toString(), DateTimeFormatter.ISO_DATE_TIME).plusDays(1),
            parameters["offset"].toString().toLong()
        )
    }

    /**
     * 인스턴스ID [instanceId] 로 인스턴스 정보를 조회한다.
     *
     */
    fun instance(instanceId: String): RestTemplateInstanceDto {
        val instance = wfInstanceRepository.findByInstanceId(instanceId)!!
        return RestTemplateInstanceDto(
            instanceId = instance.instanceId,
            documentNo = instance.documentNo,
            documentId = instance.document.documentId,
            instanceCreateUser = instance.instanceCreateUser?.userKey,
            instanceStatus = instance.instanceStatus,
            pTokenId = instance.pTokenId
        )
    }

    /**
     * Instance Create.
     */
    fun createInstance(wfTokenDto: WfTokenDto): WfInstanceEntity {
        val document = wfDocumentRepository.findDocumentEntityByDocumentId(wfTokenDto.documentId!!)
        val documentNo = numberingRuleService.getNewNumbering(document.numberingRule.numberingId)
        val user = wfTokenDto.assigneeId?.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        val instanceEntity = WfInstanceEntity(
            instanceId = "",
            documentNo = documentNo,
            instanceStatus = WfInstanceConstants.Status.RUNNING.code,
            instanceStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instanceCreateUser = user,
            pTokenId = wfTokenDto.parentTokenId,
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
            wfTokenRepository.findTopByInstanceOrderByTokenStartDtDesc(instance)?.let { token ->
                tokenDto = wfTokenMapper.toTokenDto(token)
                tokenDto.processId = token.element.processId
                tokenDto.elementId = token.element.elementId
                val tokenDataList = mutableListOf<RestTemplateTokenDataDto>()
                token.tokenDataEntities.forEach { tokenData ->
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
     * Get Instance Tags.
     */
    fun getInstanceTags(instanceId: String): List<RestTemplateTagViewDto> {
        return wfTagService.getInstanceTags(instanceId)
    }

    /**
     * Get Instance List
     */
    fun getAllInstanceListAndSearch(instanceId: String, searchValue: String): MutableList<RestTemplateInstanceListDto> {
        return wfInstanceRepository.findAllInstanceListAndSearch(instanceId, searchValue)
    }
}
