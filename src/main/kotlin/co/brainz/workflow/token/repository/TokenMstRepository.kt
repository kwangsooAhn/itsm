package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.TokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TokenMstRepository: JpaRepository<TokenEntity, String> {

    fun findTokenMstEntityByTokenId(tokenId: String): Optional<TokenEntity>
}
