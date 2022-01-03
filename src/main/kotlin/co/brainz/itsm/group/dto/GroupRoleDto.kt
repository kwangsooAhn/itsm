/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.dto

import co.brainz.framework.auth.entity.AliceRoleEntity
import java.io.Serializable

data class GroupRoleDto(
    var groupId: String = "",
    var pGroupId: String = "",
    var groupName: String? = null,
    var groupDesc: String? = null,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var roles: List<AliceRoleEntity> = emptyList()
) : Serializable


