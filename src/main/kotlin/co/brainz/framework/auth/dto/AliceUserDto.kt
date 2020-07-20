package co.brainz.framework.auth.dto

import co.brainz.framework.auth.entity.AliceMenuEntity
import co.brainz.framework.auth.entity.AliceUrlEntity
import java.io.Serializable
import java.time.LocalDateTime
import org.springframework.security.core.GrantedAuthority

/**
 * 로그인 인증시 인증 상세 정보에 담길 데이터 클래스
 */
data class AliceUserDto(
    val userKey: String,
    val userId: String,
    val userName: String,
    val email: String,
    val position: String?,
    val department: String?,
    val officeNumber: String?,
    val mobileNumber: String?,
    val useYn: Boolean,
    val tryLoginCount: Int,
    val expiredDt: LocalDateTime,
    val oauthKey: String?,
    val grantedAuthorises: Set<GrantedAuthority>,
    val menus: Set<AliceMenuEntity>,
    val urls: Set<AliceUrlEntity>,
    val timezone: String,
    val lang: String,
    val timeFormat: String,
    val theme: String
) : Serializable
