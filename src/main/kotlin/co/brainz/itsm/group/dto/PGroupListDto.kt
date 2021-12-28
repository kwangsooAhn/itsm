package co.brainz.itsm.group.dto

import java.io.Serializable

data class PGroupListDto(
    var groupId: String? = null,
    var pGroupId: String? = null,
    var groupName: String? = null,
    var groupDesc: String? = null
): Serializable
