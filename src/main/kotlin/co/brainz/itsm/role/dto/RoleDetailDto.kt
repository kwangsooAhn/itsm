package co.brainz.itsm.role.dto

import java.io.Serializable

/**
 * 사용자에서 역할 조회시 사용한다.
 */
data class RoleDetailDto(
    var roleId: String,
    var roleName: String,
    var checked: Boolean
) : Serializable
