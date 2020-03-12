package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface WfTokenRepository : JpaRepository<WfTokenEntity, String> {

    fun findTokenEntityByTokenId(tokenId: String): Optional<WfTokenEntity>

    fun findTokenMstEntityByAssigneeIdAndAssigneeTypeAndTokenStatus(assignee: String, assigneeType: String, tokenStatus: String): List<WfTokenEntity>
}
