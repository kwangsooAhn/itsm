package co.brainz.itsm.user.dto

import java.io.Serializable

public open class UserRoleMapId(
    var userKey: String = "",
    var roleId: String = ""
) : Serializable
