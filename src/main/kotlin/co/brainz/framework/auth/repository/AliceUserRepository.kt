package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceUserRepository: JpaRepository<AliceUserEntity, String> {
    fun findByUserId(userid: String): AliceUserEntity
    fun findByUserIdAndPlatform(email: String, platform: String): AliceUserEntity
}