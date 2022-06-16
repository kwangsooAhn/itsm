/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class MetricManualDto(
    val metricManualId: String = "",
    val metricName: String? = "",
    val referenceDate: LocalDate? = null,
    val metricValue: Double? = null,
    val metricUnit: String? = null,
    val createDt: LocalDateTime? = null,
    val createUserName: String? = null
) : Serializable

data class MetricManualSimpleDto(
    val metricId: String,
    val metricName: String? = ""
) : Serializable

data class MetricManualDataDto(
    val metricId: String = "",
    val referenceDate: String? = null,
    val metricValue: Double? = null
) : Serializable {
    val formattedReferenceDate: LocalDate? = LocalDate.parse(
        referenceDate, DateTimeFormatter.ISO_DATE
    )
}
