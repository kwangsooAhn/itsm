package co.brainz.workflow.instance.service

import co.brainz.workflow.comment.service.WfCommentService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.token.mapper.WfTokenMapper
import co.brainz.workflow.token.repository.WfTokenRepository
import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.numbering.service.AliceNumberingService
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.tag.service.WfTagService
import co.brainz.workflow.folder.service.WfFolderService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import java.time.ZoneId
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class WfInstanceService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository,
    private val wfCommentService: WfCommentService,
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceNumberingService: AliceNumberingService,
    private val aliceUserRepository: AliceUserRepository,
    private val wfFolderService: WfFolderService,
    private val wfTagService: WfTagService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val wfTokenMapper: WfTokenMapper = Mappers.getMapper(WfTokenMapper::class.java)

    /**
     * Search Instances.
     */
    fun instances(parameters: LinkedHashMap<String, Any>): List<RestTemplateInstanceViewDto> {
        val queryResults = when (parameters["tokenType"].toString()) {
            "token.type.requested" -> requestedInstances(parameters)
            "token.type.progress" -> relatedInstances(RestTemplateConstants.InstanceStatus.RUNNING.value, parameters)
            "token.type.completed" -> relatedInstances(RestTemplateConstants.InstanceStatus.FINISH.value, parameters)
            else -> todoInstances(parameters)
        }

        val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
        val tokens = mutableListOf<RestTemplateInstanceViewDto>()
        for (instance in queryResults.results) {

            val topicComponentIds = mutableListOf<String>()
            instance.documentEntity.form.components?.forEach {
                if (it.isTopic && componentTypeForTopicDisplay.indexOf(it.componentType) > -1) {
                    topicComponentIds.add(it.componentId)
                }
            }

            val topics = mutableListOf<String>()

            if (topicComponentIds.size > 0) {
                instance.tokenEntity.tokenData?.forEach {
                    if (topicComponentIds.indexOf(it.componentId) > -1) {
                        topics.add(it.value)
                    }
                }
            }

            tokens.add(
                RestTemplateInstanceViewDto(
                    tokenId = instance.tokenEntity.tokenId,
                    elementName = instance.tokenEntity.element.elementName,
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
                    documentColor = instance.documentEntity.documentColor,
                    totalCount = queryResults.total
                )
            )
        }

        return tokens
    }

    /**
     * 신청한 문서 조회.
     */
    private fun requestedInstances(parameters: LinkedHashMap<String, Any>): QueryResults<WfInstanceListViewDto> {
        return wfInstanceRepository.findRequestedInstances(
            parameters["userKey"].toString(),
            parameters["documentId"].toString(),
            parameters["searchValue"].toString(),
            parameters["fromDt"].toString(),
            parameters["toDt"].toString(),
            parameters["dateFormat"].toString(),
            parameters["offset"].toString().toLong()
        )
    }

    /**
     * 진행중 / 완료된 문서 조회.
     */
    private fun relatedInstances(
        status: String,
        parameters: LinkedHashMap<String, Any>
    ): QueryResults<WfInstanceListViewDto> {
        return wfInstanceRepository.findRelationInstances(
            status,
            parameters["userKey"].toString(),
            parameters["documentId"].toString(),
            parameters["searchValue"].toString(),
            parameters["fromDt"].toString(),
            parameters["toDt"].toString(),
            parameters["dateFormat"].toString(),
            parameters["offset"].toString().toLong()
        )
    }

    /**
     * 처리할 문서 조회.
     */
    private fun todoInstances(parameters: LinkedHashMap<String, Any>): QueryResults<WfInstanceListViewDto> {
        return wfInstanceRepository.findTodoInstances(
            RestTemplateConstants.TokenStatus.RUNNING.value,
            parameters["userKey"].toString(),
            parameters["documentId"].toString(),
            parameters["searchValue"].toString(),
            parameters["fromDt"].toString(),
            parameters["toDt"].toString(),
            parameters["dateFormat"].toString(),
            parameters["offset"].toString().toLong()
        )
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
    fun createInstance(wfTokenDto: WfTokenDto): WfInstanceEntity {
        val document = wfDocumentRepository.findDocumentEntityByDocumentId(wfTokenDto.documentId!!)
        val documentNo = aliceNumberingService.getNewNumbering(document.numberingRule.numberingId)
        val user = wfTokenDto.assigneeId?.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        val instanceEntity = WfInstanceEntity(
            instanceId = "",
            documentNo = documentNo,
            instanceStatus = RestTemplateConstants.InstanceStatus.RUNNING.value,
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
