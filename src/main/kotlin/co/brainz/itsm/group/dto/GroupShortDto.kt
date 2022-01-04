/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.group.dto

import java.io.Serializable

data class GroupShortDto(
    var groupId: String = "",
    var pGroupId: String? = null,
    var groupName: String? = null,
    var groupDesc: String? = null,
    var useYn: Boolean? = true
) : Serializable
