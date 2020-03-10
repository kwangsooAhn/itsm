package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.ProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProcessRepository : JpaRepository<ProcessEntity, String> {
    fun findByProcessNameLikeOrProcessDescLike(processName: String, processDesc: String): List<ProcessEntity>
    fun findProcessEntityByProcessId(processId: String): ProcessEntity
}
