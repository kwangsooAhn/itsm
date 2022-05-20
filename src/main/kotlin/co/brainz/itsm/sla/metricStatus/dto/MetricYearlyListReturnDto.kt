package co.brainz.itsm.sla.metricStatus.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class MetricYearlyListReturnDto (
    val data: List<MetricYearlyDto> = emptyList(),
    val paging: AlicePagingData
    ) :Serializable
