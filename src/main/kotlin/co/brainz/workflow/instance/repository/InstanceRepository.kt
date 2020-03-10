package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.entity.InstanceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface InstanceRepository: JpaRepository<InstanceEntity, String> {

    fun findInstanceEntityByInstanceId(instanceId: String): Optional<InstanceEntity>

    @Query("select t.tokenId as tokenId, i.instanceId as ticketId, d.documentName as ticketName, d.documentDesc as ticketDesc, i.instanceStartDt as createDt, t.assigneeId as userKey " +
            "from DocumentEntity d, InstanceEntity i, TokenEntity t " +
            "where d.processes.processId = i.processId " +
            "and i.instanceId = t.instanceId " +
            "and t.tokenStatus = :status " +
            "and t.assigneeType = :type " +
            "and t.assigneeId = :userKey")
    fun findInstances(status: String, type: String, userKey: String): List<Map<String, Any>>

}
