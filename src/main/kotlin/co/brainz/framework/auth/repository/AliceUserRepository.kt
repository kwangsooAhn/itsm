package co.brainz.framework.auth.repository

import co.brainz.framework.auth.dto.AliceUserAuthDto
import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.notice.entity.NoticeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.security.core.GrantedAuthority
import java.time.LocalDateTime

interface AliceUserRepository: JpaRepository<AliceUserEntity, String> {
    fun findByUserId(userId: String): AliceUserEntity

    @Query("SELECT a FROM AliceUserEntity a WHERE a.userId = :userId")
    fun findByUserInfo(@Param("userId") userId: String): AliceUserEntity

    fun findByOauthKeyAndPlatform(oauthKey: String, platform: String): AliceUserEntity
}