package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.WfProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfProcessRepository : JpaRepository<WfProcessEntity, String>, WfProcessRepositoryCustom {

    fun findByProcessId(processId: String): WfProcessEntity?
}
