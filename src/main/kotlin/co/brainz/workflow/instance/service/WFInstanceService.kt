package co.brainz.workflow.instance.service

import co.brainz.workflow.instance.constants.InstanceConstants
import co.brainz.workflow.instance.dto.InstanceDto
import co.brainz.workflow.instance.dto.TicketDto
import co.brainz.workflow.instance.entity.InstanceMstEntity
import co.brainz.workflow.instance.repository.InstanceMstRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class WFInstanceService(private val instanceMstRepository: InstanceMstRepository) {

    /**
     * Search Instances.
     */
    fun instances(): List<TicketDto> {

        val a = instanceMstRepository.findInstances()


        val instanceList = mutableListOf<TicketDto>()

        return instanceList
    }

    /**
     * Search Instance.
     *
     * @param instanceId
     */
    fun instance(instanceId: String) {

    }

    /**
     * Instance Create.
     *
     * @param instanceDto
     * @return InstanceMstEntity
     */
    fun createInstance(instanceDto: InstanceDto): InstanceMstEntity {
        val instanceMstEntity = InstanceMstEntity(
                instanceId = "",
                instanceStatus = instanceDto.instanceStatus?:InstanceConstants.Status.RUNNING.code,
                processId = instanceDto.processId,
                instanceStartDt = LocalDateTime.now(ZoneId.of("UTC"))
        )
        return instanceMstRepository.save(instanceMstEntity)
    }

    /**
     * Instance Complete.
     *
     * @param instanceId
     */
    fun completeInstance(instanceId: String) {
        val instanceMstEntity = instanceMstRepository.findInstanceMstEntityByInstanceId(instanceId)
        if (instanceMstEntity.isPresent) {
            instanceMstEntity.get().instanceStatus = InstanceConstants.Status.FINISH.code
            instanceMstEntity.get().instanceEndDt = LocalDateTime.now(ZoneId.of("UTC"))
            instanceMstRepository.save(instanceMstEntity.get())
        }
    }
}
