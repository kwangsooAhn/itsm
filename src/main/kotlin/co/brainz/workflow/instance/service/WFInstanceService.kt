package co.brainz.workflow.instance.service

import co.brainz.workflow.instance.constants.InstanceConstants
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.dto.TicketDto
import co.brainz.workflow.instance.entity.InstanceEntity
import co.brainz.workflow.instance.repository.InstanceRepository
import co.brainz.workflow.token.constants.TokenConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFInstanceService(private val instanceRepository: InstanceRepository) {

    /**
     * Search Instances.
     *
     * @param parameters
     * @return List<TicketDto>
     */
    fun instances(parameters: LinkedHashMap<String, Any>): List<TicketDto> {
        var status = ""
        var userKey = ""
        //TODO: assigneeType 추가시 수정
        var type = TokenConstants.AssigneeType.USER.code
        if (parameters["status"] != null) {
            status = parameters["status"].toString()
        }
        if (parameters["userKey"] != null) {
            userKey = parameters["userKey"].toString()
        }
        if (parameters["type"] != null) {
            type = parameters["type"].toString()
        }
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val ticketDataList = instanceRepository.findInstances(status, type, userKey)
        val tickets = mutableListOf<TicketDto>()
        for (ticketData in ticketDataList) {
            tickets.add(mapper.convertValue(ticketData, TicketDto::class.java))
        }

        return tickets
    }

    /**
     * Search Instance.
     *
     * @param tokenId
     */
    fun instance(tokenId: String): TicketDto {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    /**
     * Instance Create.
     *
     * @param instanceDto
     * @return InstanceEntity
     */
    fun createInstance(instanceDto: InstanceDto): InstanceEntity {
        val instanceEntity = InstanceEntity(
                instanceId = "",
                instanceStatus = instanceDto.instanceStatus?:InstanceConstants.Status.RUNNING.code,
                processId = instanceDto.processId,
                instanceStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return instanceRepository.save(instanceEntity)
    }

    /**
     * Instance Complete.
     *
     * @param instanceId
     */
    fun completeInstance(instanceId: String) {
        val instanceEntity = instanceRepository.findInstanceEntityByInstanceId(instanceId)
        if (instanceEntity.isPresent) {
            instanceEntity.get().instanceStatus = InstanceConstants.Status.FINISH.code
            instanceEntity.get().instanceEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            instanceRepository.save(instanceEntity.get())
        }
    }

}
