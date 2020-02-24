package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.TokenDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TokenDataRepository: JpaRepository<TokenDataEntity, String> {

    fun findTokenDataEntityByInstIdAndTokenId(instId: String, tokenId: String): List<TokenDataEntity>

    fun deleteTokenDataEntityByInstIdAndTokenId(instId: String, tokenId: String)
}

