package co.brainz.itsm.group.dto

import co.brainz.itsm.role.dto.RoleListDto
import java.io.Serializable

data class GroupDetailDto(
var groupId: String = "",
var pGroupId: String? = null,
var groupName: String? = null,
var groupDesc: String? = null,
var useYn: Boolean? = true
) : Serializable
