package co.brainz.workflow.engine.token.repository

import co.brainz.workflow.engine.token.entity.WfCandidateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfCandidateRepository : JpaRepository<WfCandidateEntity, String> {

}
