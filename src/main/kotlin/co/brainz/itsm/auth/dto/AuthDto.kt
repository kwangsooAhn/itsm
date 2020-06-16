package co.brainz.itsm.auth.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * 권한 조회시 사용한다.
 */
data class AuthDto(
    var authId: String?,
    var authName: String?,
    var authDesc: String?,
    var createUserKey: String?,
    var createDt: LocalDateTime = LocalDateTime.now(),
    var updateUserKey: String?,
    var updateDt: LocalDateTime?,
    var arrMenuId: List<String>?,
    var arrMenuList: MutableList<AuthMenuDto>?,
    var arrUrl: List<String>?,
    var arrUrlList: MutableList<AuthUrlDto>?,
    var roleAuthMapCount: Int
)
