package co.brainz.itsm.chart.dto

import java.io.Serializable

class ChartCalculateAverageDto(
    val count: Int = 1,
    val sum: Double = 0.00,
    var average: Double = 0.00
) : Serializable
