package co.brainz.framework.auth.entity

import co.brainz.framework.menu.AliceMenuEntity
import org.springframework.security.core.GrantedAuthority
import java.io.Serializable
import java.time.LocalDateTime

/**
 * 로그인 인증시 인증 상세 정보에 담길 데이터 클래스
 */
data class AliceUserDto(
        val userId: String,
        val userName: String,
        val email: String,
        val useYn: Boolean,
        val tryLoginCount: Int,
        val expiredDt: LocalDateTime,
        val grantedAuthories: Set<GrantedAuthority>,
        val menus: Set<AliceMenuEntity>
) : Serializable {
}