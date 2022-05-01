/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.instance.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.framework.tag.service.AliceTagManager
import co.brainz.framework.util.AlicePagingData
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.folder.service.FolderManager
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
import co.brainz.workflow.provider.dto.RestTemplateInstanceExcelDto
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
import java.time.LocalDateTime
import kotlin.math.ceil
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
    private val folderManager: FolderManager,
    private val aliceTagManager: AliceTagManager,
    private val userDetailsService: AliceUserDetailsService,
    private val currentSessionUser: CurrentSessionUser
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
        val totalCountWithoutCondition: Long

        // Get Document List
        val countSearchCondition = TokenSearchCondition(userKey = currentSessionUser.getUserKey())
        val pagingResult = when (tokenSearchCondition.searchTokenType) {
            WfTokenConstants.SearchType.REQUESTED.code -> {
                totalCountWithoutCondition = requestedInstances(countSearchCondition).totalCount
                requestedInstances(
                    tokenSearchCondition
                )
            }
            WfTokenConstants.SearchType.PROGRESS.code -> {
                totalCountWithoutCondition = relatedInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.PROGRESS),
                    countSearchCondition
                ).totalCount
                relatedInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.PROGRESS),
                    tokenSearchCondition
                )
            }
            WfTokenConstants.SearchType.COMPLETED.code -> {
                totalCountWithoutCondition = relatedInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.COMPLETED),
                    countSearchCondition
                ).totalCount
                relatedInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.COMPLETED),
                    tokenSearchCondition
                )
            }
            else -> {
                totalCountWithoutCondition = todoInstances(
                    WfInstanceConstants.getTargetStatusGroup(WfTokenConstants.SearchType.TODO),
                    WfTokenConstants.getTargetTokenStatusGroup(WfTokenConstants.SearchType.TODO),
                    countSearchCondition
                ).totalCount
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
        val tokenList = pagingResult.dataList as List<WfInstanceListViewDto>
        for (instance in tokenList) {
            tokenIds.add(instance.tokenEntity.tokenId)
        }
        if (tokenIds.isNotEmpty()) {
            tokenDataList.addAll(wfTokenDataRepository.findTokenDataByTokenIds(tokenIds))
        }

        // tags
        val instanceIds = mutableSetOf<String>()
        tokenList.forEach {
            instanceIds.add(it.instanceEntity.instanceId)
        }
        val tagList = aliceTagManager.getTagsByTargetIds(
            AliceTagConstants.TagType.INSTANCE.code,
            instanceIds
        )

        for (instance in tokenList) {
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

            val createUserAvatarPath = instance.instanceEntity.instanceCreateUser?.let { userDetailsService.makeAvatarPath(it) }
            val assigneeUserAvatarPath = instance.tokenEntity.assigneeId?.let { userDetailsService.selectUserKey(it) }
                ?.let { userDetailsService.makeAvatarPath(it) }

            val restTemplateInstanceViewDto = RestTemplateInstanceViewDto(
                tokenId = instance.tokenEntity.tokenId,
                elementName = instance.tokenEntity.element.elementName,
                instanceId = instance.instanceEntity.instanceId,
                documentName = instance.documentEntity.documentName,
                documentDesc = instance.documentEntity.documentDesc,
                documentStatus = instance.documentEntity.documentStatus,
                topics = topics,
                createDt = instance.instanceEntity.instanceStartDt,
                assigneeUserKey = instance.tokenEntity.assigneeId,
                assigneeUserId = instance.userEntity.assigneeUserId,
                assigneeUserName = instance.userEntity.assigneeUserName,
                createUserKey = instance.instanceEntity.instanceCreateUser?.userKey,
                createUserName = instance.instanceEntity.instanceCreateUser?.userName,
                documentId = instance.documentEntity.documentId,
                documentNo = instance.instanceEntity.documentNo,
                documentColor = instance.documentEntity.documentColor,
                createUserAvatarPath = createUserAvatarPath,
                assigneeUserAvatarPath = assigneeUserAvatarPath,
                documentGroupName = instance.documentEntity.documentGroupName
            )

            val tagDataList = mutableListOf<AliceTagDto>()
            tagList.forEach { tagData ->
                if (tagData.targetId == instance.instanceEntity.instanceId) {
                    tagDataList.add(tagData)
                }
            }
            restTemplateInstanceViewDto.tags = tagDataList

            tokens.add(restTemplateInstanceViewDto)
        }

        return RestTemplateInstanceListReturnDto(
            data = tokens,
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = totalCountWithoutCondition,
                currentPageNum = tokenSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / tokenSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code,
                orderColName = tokenSearchCondition.orderColName,
                orderDir = tokenSearchCondition.orderDir
            )
        )
    }

    /**
     * 신청한 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun requestedInstances(tokenSearchCondition: TokenSearchCondition): PagingReturnDto =
        wfInstanceRepository.findRequestedInstances(tokenSearchCondition)

    /**
     * 진행중 / 완료된 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun relatedInstances(
        status: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): PagingReturnDto = wfInstanceRepository.findRelationInstances(status, tokenSearchCondition)

    /**
     * 처리할 문서 조회.
     */
    @Suppress("UNCHECKED_CAST")
    private fun todoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): PagingReturnDto =
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
            val folders = folderManager.createFolder(instance)
            instance.folders = mutableListOf(folders)
            instance.pTokenId?.let { id ->
                val parentToken = wfTokenRepository.getOne(id)
                folderManager.insertInstance(parentToken.instance, instance)
                folderManager.insertInstance(instance, parentToken.instance)
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
                tokenDto.elementType = token.element.elementType
                tokenDto.assigneeId = token.assigneeId
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
        return aliceTagManager.getTagsByTargetId(
            AliceTagConstants.TagType.INSTANCE.code,
            instanceId
        )
    }

    /**
     * [instanceId] 값으로 본인을 제외한 전체 문서 목록 조회 (관련된 문서 여부를 포함)
     */
    fun findAllInstanceListByRelatedCheck(
        instanceId: String,
        searchValue: String
    ): MutableList<RestTemplateInstanceListDto> {
        return wfInstanceRepository.findAllInstanceListByRelatedCheck(instanceId, searchValue)
    }

    fun instancesForExcel(tokenSearchCondition: TokenSearchCondition): MutableList<RestTemplateInstanceExcelDto> {
        val pagingResult = when (tokenSearchCondition.searchTokenType) {
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

        // Topic
        val tokenIds = mutableSetOf<String>()
        val tokenDataList = mutableListOf<WfInstanceListTokenDataDto>()
        val instanceList = pagingResult.dataList as List<WfInstanceListViewDto>
        for (instance in instanceList) {
            tokenIds.add(instance.tokenEntity.tokenId)
        }
        if (tokenIds.isNotEmpty()) {
            tokenDataList.addAll(wfTokenDataRepository.findTokenDataByTokenIds(tokenIds))
        }
        val topics = mutableListOf<String>()
        for (instance in instanceList) {
            val topicComponentIds = mutableListOf<String>()
            tokenDataList.forEach { tokenData ->
                if (tokenData.component.isTopic &&
                    componentTypeForTopicDisplay.indexOf(tokenData.component.componentType) > -1
                ) {
                    if ((instance.tokenEntity.tokenId == tokenData.component.tokenId) && topicComponentIds.size == 0) { // 신청서의  첫번째 isTopic(제목)만 담음
                        topicComponentIds.add(tokenData.component.componentId)
                        topics.add(tokenData.value.replace(WfInstanceConstants.TOKEN_DATA_DEFAULT, ""))
                    }
                }
            }
            if(topicComponentIds.size == 0) { // 신청서에 isTopic이 1개도 없다면 강제로 빈값 추가 (Excel 다운로드시 제목란에 빈값입력을 위해)
                topics.add(" ")
            }
        }
         val tokensForExcel = mutableListOf<RestTemplateInstanceExcelDto>()

        for (instance in instanceList) {
            val restTemplateInstanceExcelDto = RestTemplateInstanceExcelDto(
                instanceStartDt = instance.instanceEntity.instanceStartDt!!,
                instanceEndDt = instance.instanceEntity.instanceEndDt,
                instanceCreateUser = instance.instanceEntity.instanceCreateUser!!.userName,
                documentName = instance.documentEntity.documentName,
                documentDesc = instance.documentEntity.documentDesc,
                documentStatus = instance.documentEntity.documentStatus,
                documentNo = instance.instanceEntity.documentNo,
                documentType = instance.documentEntity.documentType,
                documentGroupName = instance.documentEntity.documentGroupName,
                assigneeUserName= instance.userEntity.assigneeUserName,
                elementName = instance.tokenEntity.element.elementName,
                topics = topics
            )
            tokensForExcel.add(restTemplateInstanceExcelDto)
        }

        return tokensForExcel
    }

    fun getInstanceListInDocumentNo(documentNo: String): List<WfInstanceEntity> {
        return wfInstanceRepository.findWfInstanceEntitiesByDocumentNo(documentNo)
    }
}
