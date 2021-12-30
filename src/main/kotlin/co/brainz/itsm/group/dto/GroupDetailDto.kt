package co.brainz.itsm.group.dto

import co.brainz.framework.auth.entity.AliceRoleEntity
import java.io.Serializable

data class GroupDetailDto(
    var groupId: String = "",
    var pGroupId: String = "",
    var groupName: String? = null,
    var groupDesc: String? = null,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var allRoles: List<AliceRoleEntity> = emptyList(),
    var groupUseRoles: List<AliceRoleEntity> = emptyList()
) : Serializable


