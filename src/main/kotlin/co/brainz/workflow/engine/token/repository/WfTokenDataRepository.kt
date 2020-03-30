package co.brainz.workflow.engine.token.repository

import co.brainz.workflow.engine.token.entity.WfTokenDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfTokenDataRepository : JpaRepository<WfTokenDataEntity, String> {

    fun deleteTokenDataEntityByTokenId(tokenId: String)

    fun findTokenDataEntityByTokenId(tokenId: String): List<WfTokenDataEntity>

    fun findByTokenIdAndComponentId(
        tokenId: String,
        componentId: String
    ): WfTokenDataEntity
}

