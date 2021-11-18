package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartComponentDataDto(
    val componentId: String,
    val componentValue: String? = null,
    val componentType: String,
    val tagValue: String? = null
) : Serializable
