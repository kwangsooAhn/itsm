/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import co.brainz.framework.tag.dto.AliceTagDto
import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class ChartDto(
    var chartId: String = "",
    var chartType: String = "",
    var chartName: String = "",
    var chartDesc: String? = null,
    var chartConfig: ChartConfig,
    var createDt: LocalDateTime? = null,
    var propertyJson: String? = null,
    var chartConfigStr: String? = null,
    var tags: List<AliceTagDto> = emptyList()
) : Serializable

data class ChartConfig(
    var range: ChartRange,
    var operation: String = "",
    var periodUnit: String? = null
) : Serializable

data class ChartRange(
    var type: String,
    @JsonFormat(pattern = "yyyy-MM-dd")
    var from: LocalDate? = null,
    @JsonFormat(pattern = "yyyy-MM-dd")
    var to: LocalDate? = null
) : Serializable
