package co.brainz.workflow.engine.instance.service

import co.brainz.workflow.engine.instance.constants.WfInstanceConstants
import co.brainz.workflow.engine.instance.dto.WfInstanceCountDto
import co.brainz.workflow.engine.instance.dto.WfInstanceDto
import co.brainz.workflow.engine.instance.dto.WfInstanceHistoryDto
import co.brainz.workflow.engine.instance.dto.WfInstanceViewDto
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.instance.repository.WfInstanceRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WfInstanceService(private val wfInstanceRepository: WfInstanceRepository) {

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
     * Search Instance.
     *
     * @param tokenId
     */
    fun instance(tokenId: String): WfInstanceViewDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        var userKey: String = ""
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
}
