package co.brainz.workflow.token.repository

import co.brainz.workflow.token.entity.TokenMstEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface TokenMstRepository: JpaRepository<TokenMstEntity, String> {

    fun findTokenMstEntityByTokenId(tokenId: String): Optional<TokenMstEntity>
}
