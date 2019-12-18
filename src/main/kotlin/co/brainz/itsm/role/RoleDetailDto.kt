package co.brainz.itsm.role

import java.io.Serializable


data class RoleDetailDto(
        var roleId: String,
        var roleName: String,
        var checked: Boolean
) : Serializable