package co.brainz.framework.auth.dto

import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.AliceUrlEntity
import java.io.Serializable
import java.time.LocalDateTime
import org.springframework.security.core.GrantedAuthority

/**
 * 로그인 인증시 인증 상세 정보에 담길 데이터 클래스
 */
data class AliceUserAuthDto(
    var userKey: String = "",
    var userId: String = "",
    var userName: String = "",
    var password: String = "",
    var email: String = "",
    var position: String? = "",
    var department: String? = "",
    var officeNumber: String? = "",
    var mobileNumber: String? = "",
    var useYn: Boolean = true,
    var tryLoginCount: Int = 0,
    var expiredDt: LocalDateTime = LocalDateTime.now(),
    var oauthKey: String? = "",
    var timezone: String = "",
    var lang: String = "",
    var timeFormat: String = "",
    var theme: String = "",
    var grantedAuthorises: Set<GrantedAuthority>? = null,
    var menus: Set<AliceMenuEntity>? = null,
    var urls: Set<AliceUrlEntity>? = null
) : Serializable
