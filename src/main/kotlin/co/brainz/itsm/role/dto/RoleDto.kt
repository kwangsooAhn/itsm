package co.brainz.itsm.role.dto

import java.time.LocalDateTime
import com.fasterxml.jackson.annotation.JsonFormat
import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import co.brainz.framework.auth.entity.AliceUserEntity

/**
 * 역할 조회시 사용한다.
 */
data class RoleDto(
        var roleId: String?,
        var roleName: String?,
        var roleDesc: String?,
        var createUserName: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var createDt: LocalDateTime = LocalDateTime.now(),
        var updateUserName: String?,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
        var updateDt: LocalDateTime?,
        var arrAuthId: MutableSet<String>?,
        var arrAuthList: MutableList<AliceAuthSimpleDto>?
)
