package co.brainz.itsm.role

import java.time.LocalDateTime
import co.brainz.itsm.auth.AuthEntity

data public class RoleDto (
    var roleId: String?,
    var roleName: String?,
    var roleDesc: String?,
    var createUserid: String?,
    var createDt: LocalDateTime?,
    var updateUserid: String?,
    var updateeDt: LocalDateTime?,
    var arrAuthId: Array<String>? = null,
    var arrAuthList: List<AuthEntity>?
)
