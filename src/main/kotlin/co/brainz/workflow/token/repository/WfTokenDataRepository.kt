package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.WfTokenDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfTokenDataRepository : JpaRepository<WfTokenDataEntity, String> {

    fun findWfTokenDataEntitiesByTokenTokenId(tokenId: String): List<WfTokenDataEntity>

    fun findWfTokenDataEntitiesByTokenTokenIdAndComponentComponentId(
        tokenId: String,
        componentId: String
    ): WfTokenDataEntity
}
