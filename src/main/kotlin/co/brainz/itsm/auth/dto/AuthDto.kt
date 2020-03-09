package co.brainz.itsm.auth.dto

import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat

/**
 * 권한 조회시 사용한다.
 */
data class AuthDto(
        var authId: String?,
        var authName: String?,
        var authDesc: String?,
        var createUserKey: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var createDt: LocalDateTime = LocalDateTime.now(),
        var updateUserKey: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var updateDt: LocalDateTime?,
        var arrMenuId: List<String>?,
        var arrMenuList: MutableList<AuthMenuDto>?,
        var arrUrl: List<String>?,
        var arrUrlList: MutableList<AuthUrlDto>?,
        var roleAuthMapCount: Int
)
