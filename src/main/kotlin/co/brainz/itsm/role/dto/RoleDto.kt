package co.brainz.itsm.role.dto

import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 * 역할 조회시 사용한다.
 */
data class RoleDto(
    var roleId: String?,
    var roleName: String?,
    var roleDesc: String?,
    var createUserName: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var createDt: LocalDateTime?,
    var updateUserName: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updateDt: LocalDateTime?,
    var arrAuthId: MutableSet<String>?,
    var arrAuthList: MutableList<AliceAuthSimpleDto>?,
    var userRoleMapCount: Int
)
