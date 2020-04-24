package co.brainz.workflow.engine.token.repository

import co.brainz.workflow.engine.token.entity.WfTokenEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfTokenRepository : JpaRepository<WfTokenEntity, String> {

    fun findTokenEntityByTokenId(tokenId: String): Optional<WfTokenEntity>

    fun findTokenMstEntityByAssigneeIdAndTokenStatus(assignee: String, tokenStatus: String): List<WfTokenEntity>
}
