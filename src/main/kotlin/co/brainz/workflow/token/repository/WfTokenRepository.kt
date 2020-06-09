package co.brainz.workflow.token.repository

import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfTokenEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfTokenRepository : JpaRepository<WfTokenEntity, String> {

    fun findTokenEntityByTokenId(tokenId: String): Optional<WfTokenEntity>

    fun findTokenMstEntityByAssigneeIdAndTokenStatus(assignee: String, tokenStatus: String): List<WfTokenEntity>

    /**
     * 인스턴스ID[instance] 로 마지막[tokenStatus] token  정보를 조회한다.
     */
    fun findTopByInstanceAndTokenStatusOrderByTokenStartDtDesc(
        instance: WfInstanceEntity,
        tokenStatus: String = WfTokenConstants.Status.FINISH.code
    ): WfTokenEntity?
}
