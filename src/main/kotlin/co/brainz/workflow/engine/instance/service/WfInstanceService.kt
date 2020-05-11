package co.brainz.workflow.engine.instance.service

import co.brainz.workflow.engine.comment.mapper.WfCommentMapper
import co.brainz.workflow.engine.instance.constants.WfInstanceConstants
import co.brainz.workflow.engine.instance.dto.WfInstanceCountDto
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.dto.WfInstanceHistoryDto
import co.brainz.workflow.engine.instance.dto.WfInstanceViewDto
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import co.brainz.workflow.engine.token.dto.WfTokenDataDto
import co.brainz.workflow.engine.token.dto.WfTokenDto
import co.brainz.workflow.engine.token.mapper.WfTokenMapper
import co.brainz.workflow.engine.token.repository.WfTokenRepository
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
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
    private val wfTokenRepository: WfTokenRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val wfTokenMapper: WfTokenMapper = Mappers.getMapper(WfTokenMapper::class.java)
    private val wfCommentMapper: WfCommentMapper = Mappers.getMapper(WfCommentMapper::class.java)

    /**
     * Search Instances.
     */
    fun instances(parameters: LinkedHashMap<String, Any>): List<WfInstanceViewDto> {
        var status = ""
        var userKey = ""

        if (parameters["status"] != null) {
            status = parameters["status"].toString()
        }
        if (parameters["userKey"] != null) {
            userKey = parameters["userKey"].toString()
        }

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val tokenDataList = wfInstanceRepository.findInstances(status, userKey)
        val tokens = mutableListOf<WfInstanceViewDto>()
        for (tokenData in tokenDataList) {
            tokens.add(mapper.convertValue(tokenData, WfInstanceViewDto::class.java))
        }

        return tokens
    }

    /**
     * 인스턴스ID [instanceId] 로 인스턴스 정보를 조회한다.
     *
     */
    fun instance(instanceId: String): WfInstanceViewDto {
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.convertValue(
            wfInstanceRepository.findByInstanceId(instanceId),
            WfInstanceViewDto::class.java
        )
    }

    /**
     * Instance Create.
     */
    fun createInstance(wfInstanceDto: WfInstanceDto): WfInstanceEntity {
        val instanceEntity = WfInstanceEntity(
            instanceId = "",
            instanceStatus = wfInstanceDto.instanceStatus ?: WfInstanceConstants.Status.RUNNING.code,
            document = wfInstanceDto.document,
            instanceStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        if (wfInstanceDto.pTokenId != null) {
            instanceEntity.pTokenId = wfInstanceDto.pTokenId
        }
        instanceEntity.documentNo = wfInstanceDto.documentNo

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
    fun instancesStatusCount(parameters: LinkedHashMap<String, Any>): List<WfInstanceCountDto> {
        var userKey = ""
        if (parameters["userKey"] != null) {
            userKey = parameters["userKey"].toString()
        }
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val instanceStatusCountList = wfInstanceRepository.findInstancesCount(userKey)
        val counts = mutableListOf<WfInstanceCountDto>()
        for (instanceStatusCount in instanceStatusCountList) {
            counts.add(mapper.convertValue(instanceStatusCount, WfInstanceCountDto::class.java))
        }

        return counts
    }

    /**
     * Instance history
     */
    fun getInstancesHistory(instanceId: String): List<WfInstanceHistoryDto> {
        return wfInstanceRepository.findInstanceHistory(instanceId)
    }

    /**
     * 인스턴스ID[instanceId]로 마지막 토큰 정보를 조회한다.
     */
    fun getInstanceLatestToken(instanceId: String): WfTokenDto {
        var tokenDto = WfTokenDto()
        wfInstanceRepository.findByInstanceId(instanceId)?.let { instance ->
            wfTokenRepository.findTopByInstanceAndTokenStatusOrderByTokenStartDtDesc(instance)?.let { token ->
                tokenDto = wfTokenMapper.toTokenDto(token)
                val tokenDatas = mutableListOf<WfTokenDataDto>()
                token.tokenDatas?.forEach { tokenData ->
                    tokenDatas.add(wfTokenMapper.toTokenDataDto(tokenData))
                }
                tokenDto.data = tokenDatas
            }
        }

        logger.debug("Latest token: {}", tokenDto)
        return tokenDto
    }

    /**
     * Get Instance Comments.
     */
    fun getInstanceComments(instanceId: String): MutableList<RestTemplateCommentDto> {
        val commentList: MutableList<RestTemplateCommentDto> = mutableListOf()
        val instanceEntity = wfInstanceRepository.findByInstanceId(instanceId)
        instanceEntity?.comments?.forEach { comment ->
            commentList.add(wfCommentMapper.toCommentDto(comment))
        }

        return commentList
    }
}
