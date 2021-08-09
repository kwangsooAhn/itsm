package co.brainz.itsm.chart.dto

import co.brainz.itsm.constants.ItsmConstants
import java.io.Serializable

data class ChartSearchDto(
    val search: String? = null,
    val offset: Long? = 0,
    val limit: Long? = ItsmConstants.SEARCH_DATA_COUNT
) : Serializable
