package co.brainz.workflow.engine.process.repository

import co.brainz.workflow.engine.process.entity.WfProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfProcessRepository : JpaRepository<WfProcessEntity, String> {
    @Query("SELECT m " +
            "FROM WfProcessEntity m " +
            "WHERE (lower(m.processName) like lower(concat('%', :value, '%')) or lower(m.processDesc) like lower(concat('%', :value, '%')) or :value is null or :value = '') " +
            "ORDER BY " +
            "CASE " +
            "WHEN m.processStatus = 'process.status.edit' THEN 1 " +
            "WHEN m.processStatus = 'process.status.publish' THEN 2 " +
            "WHEN m.processStatus = 'process.status.use' THEN 3 " +
            "WHEN m.processStatus = 'process.status.destroy' THEN 4 " +
            "END, COALESCE(m.updateDt, m.createDt) DESC")
    fun findByProcessListOrProcessSearchList(value: String?): List<WfProcessEntity>
    fun findByProcessStatusInOrderByProcessName(processStatus: List<String>): List<WfProcessEntity>
    fun findByProcessId(processId: String): WfProcessEntity?
}
