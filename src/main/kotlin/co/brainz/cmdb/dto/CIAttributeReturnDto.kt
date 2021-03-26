package co.brainz.cmdb.dto

import java.io.Serializable

data class CIAttributeReturnDto(
    val data: List<CIAttributeListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
