package co.brainz.itsm.sla.metricYear.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class MetricAnnualListReturnDto(
    val data: List<MetricAnnualDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
