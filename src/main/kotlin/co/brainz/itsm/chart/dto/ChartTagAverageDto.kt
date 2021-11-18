package co.brainz.itsm.chart.dto

import java.io.Serializable

class ChartTagAverageDto(
    val tagValue: String? = null,
    val average: Long? = null
) : Serializable
