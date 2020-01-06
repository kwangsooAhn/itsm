package co.brainz.itsm.user

import java.io.Serializable

public open class UserRoleMapId(
    var userId: String = "",
    var roleId: String = ""
) : Serializable