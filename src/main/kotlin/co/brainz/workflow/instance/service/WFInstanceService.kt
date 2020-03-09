package co.brainz.workflow.instance.service

import co.brainz.workflow.instance.constants.InstanceConstants
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.dto.TicketDto
import co.brainz.workflow.instance.entity.InstanceEntity
import co.brainz.workflow.instance.repository.InstanceRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFInstanceService(private val instanceRepository: InstanceRepository) {

    /**
     * Search Instances.
     */
    fun instances(parameters: LinkedHashMap<String, Any>): List<TicketDto> {
        var status = ""
        var userKey = ""
        if (parameters["status"] != null) {
            status = parameters["status"].toString()
        }
        if (parameters["userKey"] != null) {
            userKey = parameters["userKey"].toString()
        }
        return instanceRepository.findInstances(status, userKey)
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
     * @return InstanceMstEntity
     */
    fun createInstance(instanceDto: InstanceDto): InstanceEntity {
        val instanceMstEntity = InstanceEntity(
                instanceId = "",
                instanceStatus = instanceDto.instanceStatus?:InstanceConstants.Status.RUNNING.code,
                processId = instanceDto.processId,
                instanceStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return instanceRepository.save(instanceMstEntity)
    }

    /**
     * Instance Complete.
     *
     * @param instanceId
     */
    fun completeInstance(instanceId: String) {
        val instanceMstEntity = instanceRepository.findInstanceEntityByInstanceId(instanceId)
        if (instanceMstEntity.isPresent) {
            instanceMstEntity.get().instanceStatus = InstanceConstants.Status.FINISH.code
            instanceMstEntity.get().instanceEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            instanceRepository.save(instanceMstEntity.get())
        }
    }

}
