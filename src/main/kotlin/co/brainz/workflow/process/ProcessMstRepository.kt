package co.brainz.workflow.process

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessMstRepository : JpaRepository<ProcessMstEntity, String> {
    fun findByProcNameLikeOrProcDescLike(procName: String, procDesc: String): List<ProcessMstEntity>
}
