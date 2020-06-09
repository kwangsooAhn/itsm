package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfCandidateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfCandidateRepository : JpaRepository<WfCandidateEntity, String> {

}
