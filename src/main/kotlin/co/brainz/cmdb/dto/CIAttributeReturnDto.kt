package co.brainz.cmdb.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class CIAttributeReturnDto(
    val data: List<CIAttributeListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
