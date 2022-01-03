/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.dto

import java.io.Serializable

data class GroupListDto(
    var data: List<GroupDto> = emptyList(),
    var totalCount: Long = 0L
) : Serializable
