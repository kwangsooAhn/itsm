/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.dto

import co.brainz.itsm.role.dto.RoleListDto
import java.io.Serializable

data class GroupDetailDto(
    var groupId: String = "",
    var pGroupId: String = "",
    var groupName: String? = null,
    var groupDesc: String? = null,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var allRoles: List<RoleListDto> = emptyList(),
    var groupUseRoles: List<RoleListDto> = emptyList()
) : Serializable


