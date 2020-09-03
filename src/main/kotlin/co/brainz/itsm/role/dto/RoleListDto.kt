package co.brainz.itsm.role.dto

import java.io.Serializable

data class RoleListDto(
    var roleId: String,
    var roleName: String,
    var roleDesc: String?
) : Serializable
