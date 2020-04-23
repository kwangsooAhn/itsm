package co.brainz.workflow.engine.instance.service

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
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfInstanceService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfTokenRepository: WfTokenRepository
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val wfTokenMapper: WfTokenMapper = Mappers.getMapper(WfTokenMapper::class.java)

    /**
     * Search Instances.
     *
     * @param parameters
     * @return List<WfInstanceViewDto>
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
        TODO("")
    }

    /**
     * Instance Create.
     *
     * @param wfInstanceDto
     * @return InstanceEntity
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
        return wfInstanceRepository.save(instanceEntity)
    }

    /**
     * Instance Complete.
     *
     * @param instanceId
     */
    fun completeInstance(instanceId: String) {
        val instanceEntity = wfInstanceRepository.findInstanceEntityByInstanceId(instanceId)
        if (instanceEntity.isPresent) {
            instanceEntity.get().instanceStatus = WfInstanceConstants.Status.FINISH.code
            instanceEntity.get().instanceEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            wfInstanceRepository.save(instanceEntity.get())
        }
    }

    /**
     * Instance Status Count
     *
     * @param parameters
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
    fun getInstanceLastestToken(instanceId: String): WfTokenDto {
        val instanceEntity = wfInstanceRepository.getOne(instanceId)
        val tokenEntity = wfTokenRepository.findTopByInstanceAndTokenStatusOrderByTokenStartDtDesc(instanceEntity)

        val tokenDto = wfTokenMapper.toTokenDto(tokenEntity)
        val tokenDatas = mutableListOf<WfTokenDataDto>()
        tokenEntity.tokenDatas?.forEach {
            tokenDatas.add(wfTokenMapper.toTokenDataDto(it))
        }
        tokenDto.data = tokenDatas

        logger.debug("Lastest token: {}", tokenDto)
        return tokenDto
    }
}
