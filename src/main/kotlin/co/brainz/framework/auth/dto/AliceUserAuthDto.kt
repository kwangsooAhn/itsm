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
        var userKey: String = "",
        var userId: String = "",
        var userName: String = "",
        var password: String = "",
        var email: String = "",
        var useYn: Boolean = true,
        var tryLoginCount: Int = 0,
        var expiredDt: LocalDateTime = LocalDateTime.now(),
        var oauthKey: String? = "",
        var timezone: String = "",
        var lang: String = "",
        var timeFormat: String = "",
        var theme: String = "default",
        var grantedAuthorises: Set<GrantedAuthority>? = null,
        var menus: Set<AliceMenuEntity>? = null,
        var urls: Set<AliceUrlEntity>? = null
) : Serializable
