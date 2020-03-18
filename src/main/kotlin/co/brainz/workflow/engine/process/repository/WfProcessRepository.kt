package co.brainz.workflow.engine.process.repository

import co.brainz.workflow.engine.process.entity.WfProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfProcessRepository : JpaRepository<WfProcessEntity, String> {
    fun findByProcessNameLikeOrProcessDescLike(processName: String, processDesc: String): List<WfProcessEntity>
    fun findByProcessId(processId: String): WfProcessEntity

    @Query("SELECT m " +
            "FROM WfProcessEntity m " +
            "ORDER BY CASE " +
            "WHEN m.processStatus = 'process.status.edit' THEN 1 " +
            "WHEN m.processStatus = 'process.status.publish' THEN 2 " +
            "WHEN m.processStatus = 'process.status.destroy' THEN 3 " +
            "END, COALESCE(m.updateDt, m.createDt) DESC")
    fun findByProcessList(): List<WfProcessEntity>
}
