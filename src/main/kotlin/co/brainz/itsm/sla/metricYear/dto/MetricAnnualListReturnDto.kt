package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricAnnualListReturnDto(
    val data: List<MetricAnnualDto> = emptyList(),
    val totalCount: Long? = 0L,
    val totalCountWithoutCondition: Long? = 0L
) : Serializable
