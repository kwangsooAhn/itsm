package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.dto.TicketDto
import co.brainz.workflow.instance.entity.InstanceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface InstanceMstRepository: JpaRepository<InstanceEntity, String> {

    fun findInstanceMstEntityByInstanceId(instanceId: String): Optional<InstanceEntity>

    @Query(nativeQuery = true, name = "findInstances")
    fun findInstances(status: String, userKey: String): List<TicketDto>

}
