package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceAuthRepository: JpaRepository<AliceAuthEntity, String> {
    fun findByAuthIdIn(authId: Set<String>): Set<AliceAuthEntity>
}
