package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.entity.WfInstanceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface WfInstanceRepository: JpaRepository<WfInstanceEntity, String> {

    fun findInstanceEntityByInstanceId(instanceId: String): Optional<WfInstanceEntity>

    @Query("select t.tokenId as tokenId, i.instanceId as ticketId, d.documentName as ticketName, d.documentDesc as ticketDesc, i.instanceStartDt as createDt, t.assigneeId as userKey " +
            "from WfDocumentEntity d, WfInstanceEntity i, WfTokenEntity t " +
            "where d.documentId = i.document.documentId " +
            "and i.instanceId = t.instance.instanceId " +
            "and t.tokenStatus = :status " +
            "and t.assigneeType = :type " +
            "and t.assigneeId = :userKey")
    fun findInstances(status: String, type: String, userKey: String): List<Map<String, Any>>

}
