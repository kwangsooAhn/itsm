/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.itsm.folder.service.FolderService
import co.brainz.itsm.numberingRule.service.NumberingRuleService
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.mapper.WfTokenMapper
import co.brainz.workflow.token.repository.WfTokenDataRepository
import co.brainz.workflow.token.repository.WfTokenRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
class WfInstanceService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfTokenDataRepository: WfTokenDataRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val numberingRuleService: NumberingRuleService,
    private val aliceUserRepository: AliceUserRepository,
    private val folderService: FolderService,
    private val aliceTagService: AliceTagService,
    private val userDetailsService: AliceUserDetailsService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val wfTokenMapper: WfTokenMapper = Mappers.getMapper(WfTokenMapper::class.java)

    /**
     * String 데이터를 분리하여 Set<String> 으로 변환
     */
    private fun stringToSet(args: String): HashSet<String> {
        val set = HashSet<String>()
        if (args.isNotEmpty()) {
            set.addAll(
                StringUtils.trimAllWhitespace(args).replace("#", "").split(",")
            )
        }
        return set
    }

    /**
     * Search Instances.
     */
    fun instances(tokenSearchCondition: TokenSearchCondition): RestTemplateInstanceListReturnDto {
        // Get Document List
        val queryResults = when (tokenSearchCondition.searchTokenType) {
            WfTokenConstants.SearchType.REQUESTED.code -> {
                requestedInstances(
                    tokenSearchCondition
                )
            }
            WfTokenConstants.SearchType.PROGRESS.code -> {
                relatedInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.PROGRESS),
                    tokenSearchCondition
                )
            }
            WfTokenConstants.SearchType.COMPLETED.code -> {
                relatedInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.COMPLETED),
                    tokenSearchCondition
                )
            }
            else -> {
                todoInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.TODO),
                    WfTokenConstants.getTargetTokenStatusGroup(WfTokenConstants.SearchType.TODO),
                    tokenSearchCondition
                )
            }
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

        for (instance in queryResults.results) {
            val topics = mutableListOf<String>()
            val topicComponentIds = mutableListOf<String>()
            tokenDataList.forEach { tokenData ->
                if (tokenData.component.isTopic &&
                    componentTypeForTopicDisplay.indexOf(tokenData.component.componentType) > -1
                ) {
                    if (instance.tokenEntity.tokenId == tokenData.component.tokenId) {
                        topicComponentIds.add(tokenData.component.componentId)
                        topics.add(tokenData.value.replace(WfInstanceConstants.TOKEN_DATA_DEFAULT, ""))
                    }
                }
            }

            val avatarPath = instance.instanceEntity.instanceCreateUser?.let {
                userDetailsService.makeAvatarPath(it)
            }

            // Tag
            val tagDataList = aliceTagService.getTagsByTargetId(
                AliceTagConstants.TagType.INSTANCE.code,
                instance.instanceEntity.instanceId
            )

            tokens.add(
                RestTemplateInstanceViewDto(
                    tokenId = instance.tokenEntity.tokenId,
                    elementName = instance.tokenEntity.element.elementName,
                    instanceId = instance.instanceEntity.instanceId,
                    documentName = instance.documentEntity.documentName,
                    documentDesc = instance.documentEntity.documentDesc,
                    documentStatus = instance.documentEntity.documentStatus,
                    topics = topics,
                    tags = tagDataList,
                    createDt = instance.instanceEntity.instanceStartDt,
                    assigneeUserKey = instance.tokenEntity.assigneeId,
                    assigneeUserName = "",
                    createUserKey = instance.instanceEntity.instanceCreateUser?.userKey,
                    createUserName = instance.instanceEntity.instanceCreateUser?.userName,
                    documentId = instance.documentEntity.documentId,
                    documentNo = instance.instanceEntity.documentNo,
                    documentColor = instance.documentEntity.documentColor,
                    avatarPath = avatarPath
                )
            )
        }

        return RestTemplateInstanceListReturnDto(
            data = tokens,
            totalCount = queryResults.total
        )
    }

    /**
     * 신청한 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun requestedInstances(tokenSearchCondition: TokenSearchCondition): QueryResults<WfInstanceListViewDto> =
        wfInstanceRepository.findRequestedInstances(tokenSearchCondition)

    /**
     * 진행중 / 완료된 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun relatedInstances(
        status: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): QueryResults<WfInstanceListViewDto> = wfInstanceRepository.findRelationInstances(status, tokenSearchCondition)

    /**
     * 처리할 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun todoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): QueryResults<WfInstanceListViewDto> =
        wfInstanceRepository.findTodoInstances(status, tokenStatus, tokenSearchCondition)

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
            instanceId = wfTokenDto.instanceId,
            documentNo = documentNo,
            instanceStatus = WfInstanceConstants.Status.RUNNING.code,
            instanceStartDt = LocalDateTime.now(),
            instanceCreateUser = user,
            pTokenId = wfTokenDto.parentTokenId,
            document = document,
            instancePlatform = wfTokenDto.instancePlatform
        )
        val instance = wfInstanceRepository.save(instanceEntity)
        instance.let {
            val folders = folderService.createFolder(instance)
            instance.folders = mutableListOf(folders)
            instance.pTokenId?.let { id ->
                val parentToken = wfTokenRepository.getOne(id)
                folderService.insertInstance(parentToken.instance, instance)
                folderService.insertInstance(instance, parentToken.instance)
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
            it.instanceEndDt = LocalDateTime.now()
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
    fun getInstancesHistory(instanceId: String): MutableList<RestTemplateInstanceHistoryDto> {
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
     * Get Instance Tags.
     */
    fun getInstanceTags(instanceId: String): List<AliceTagDto> {
        return aliceTagService.getTagsByTargetId(
            AliceTagConstants.TagType.INSTANCE.code,
            instanceId
        )
    }

    /**
     * Get Instance List
     */
    fun getAllInstanceListAndSearch(instanceId: String, searchValue: String): MutableList<RestTemplateInstanceListDto> {
        return wfInstanceRepository.findAllInstanceListAndSearch(instanceId, searchValue)
    }
}
