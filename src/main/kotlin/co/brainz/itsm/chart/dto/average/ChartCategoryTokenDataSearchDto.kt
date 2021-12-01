package co.brainz.itsm.chart.dto.average

import java.io.Serializable

data class ChartCategoryTokenDataSearchDto(
    val category: String,
    val tokenId: String,
    val componentId: String
) : Serializable
