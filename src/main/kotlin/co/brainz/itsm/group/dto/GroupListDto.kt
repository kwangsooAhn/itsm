package co.brainz.itsm.group.dto

import java.io.Serializable

data class GroupListDto(
    var data: List<GroupDto> = emptyList(),
    var totalCount: Long = 0L
) : Serializable
