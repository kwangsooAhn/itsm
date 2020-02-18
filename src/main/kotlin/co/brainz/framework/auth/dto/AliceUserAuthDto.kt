package co.brainz.framework.auth.dto

import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceMenuEntity
import org.springframework.security.core.GrantedAuthority
import java.io.Serializable
import java.time.LocalDateTime

/**
 * 로그인 인증시 인증 상세 정보에 담길 데이터 클래스
 */
data class AliceUserAuthDto(
        val userKey: String,
        val userId: String,
        val userName: String,
        val password: String,
        val email: String,
        val useYn: Boolean,
        val tryLoginCount: Int,
        val expiredDt: LocalDateTime,
        val oauthKey: String?,
        val timezone: String,
        val lang: String,
        val timeFormat: String,
        var grantedAuthories: Set<GrantedAuthority>,
        var menus: Set<AliceMenuEntity>,
        var urls: Set<AliceUrlEntity>
) : Serializable
