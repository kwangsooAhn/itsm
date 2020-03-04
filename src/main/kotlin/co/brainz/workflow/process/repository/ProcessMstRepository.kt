package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.ProcessMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessMstRepository : JpaRepository<ProcessMstEntity, String> {
    fun findByProcessNameLikeOrProcessDescLike(processName: String, processDesc: String): List<ProcessMstEntity>
    fun findProcessMstEntityByProcessId(processId: String): ProcessMstEntity
}
