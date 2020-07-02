package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfCandidateRepository : JpaRepository<WfCandidateEntity, String> {
    @Query("select f from WfCandidateEntity f where f.token = :tokenEntity and f.candidateType = :candidateType")
    fun findByTokenIdAndCandidateType(tokenEntity: WfTokenEntity, candidateType: String): List<WfCandidateEntity>
}
