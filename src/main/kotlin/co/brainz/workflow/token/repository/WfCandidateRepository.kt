package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface WfCandidateRepository : JpaRepository<WfCandidateEntity, String> {
    @Query("select f from WfCandidateEntity f where f.token = :tokenEntity and f.candidateType = :candidateType")
    fun findByTokenIdAndCandidateType(tokenEntity: Optional<WfTokenEntity>, candidateType: String): List<WfCandidateEntity>
}
