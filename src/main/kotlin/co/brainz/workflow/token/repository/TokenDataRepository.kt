package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.TokenDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenDataRepository: JpaRepository<TokenDataEntity, String> {

    fun deleteTokenDataEntityByInstanceIdAndTokenId(instanceId: String, tokenId: String)
}

