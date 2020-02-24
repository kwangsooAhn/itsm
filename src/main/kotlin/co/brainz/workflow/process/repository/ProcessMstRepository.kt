package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.ProcessMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessMstRepository : JpaRepository<ProcessMstEntity, String> {
    fun findByProcNameLikeOrProcDescLike(procName: String, procDesc: String): List<ProcessMstEntity>
    fun findByProcId(procId: String): ProcessMstEntity
}
