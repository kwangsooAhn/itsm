package co.brainz.itsm.portal.dto

import java.io.Serializable

data class PortalListReturnDto(
    val data: List<PortalDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable

