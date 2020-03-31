package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional

interface WfInstanceRepository: JpaRepository<WfInstanceEntity, String> {

    fun findInstanceEntityByInstanceId(instanceId: String): Optional<WfInstanceEntity>

    @Query("select t.tokenId as tokenId, i.instanceId as instanceId, d.documentName as documentName, d.documentDesc as documentDesc, i.instanceStartDt as createDt, t.assigneeId as userKey " +
            "from WfDocumentEntity d, WfInstanceEntity i, WfTokenEntity t " +
            "where d.documentId = i.document.documentId " +
            "and i.instanceId = t.instance.instanceId " +
            "and t.tokenStatus = :status " +
            "and t.assigneeType = :type " +
            "and t.assigneeId = :userKey")
    fun findInstances(status: String, type: String, userKey: String): List<Map<String, Any>>

    fun countByDocument(wfDocumentEntity: WfDocumentEntity): Int

    @Query("select i.instanceStatus as instanceStatus, count(i.instanceStatus) as instanceCount " +
            "from WfInstanceEntity i " +
            "where i.instanceCreateUserKey = :userKey " +
            "group by i.instanceStatus " +
            "order by " +
            "case " +
            "   when i.instanceStatus = 'running' then 1  " +
            "   when i.instanceStatus = 'wait' then 2 " +
            "   when i.instanceStatus = 'finish' then 3 " +
            "end" )
    fun findInstancesCount(userKey: String): List<Map<String, Any>>
}
