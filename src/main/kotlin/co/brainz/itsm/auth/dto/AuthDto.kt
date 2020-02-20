package co.brainz.itsm.auth.dto

import co.brainz.framework.auth.entity.AliceAuthEntity
import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat
import co.brainz.framework.auth.dto.AliceAuthSimpleDto

/**
 * 역할 조회시 사용한다.
 */
data class AuthDto(
        var authId: String?,
        var authName: String?,
        var authDesc: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var createDt: LocalDateTime = LocalDateTime.now(),
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var updateDt: LocalDateTime?
    
        // menu
//        var arrMenuId: MutableSet<String>?,
//        var arrMenuList: MutableList<AliceAuthSimpleDto>?
)
