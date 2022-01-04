/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.group.dto

import java.io.Serializable

data class PGroupDto(
    var groupId: String? = null,
    var pGroupId: String? = null,
    var groupName: String? = null,
    var groupDesc: String? = null
) : Serializable
