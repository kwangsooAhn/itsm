/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class MetricManualDataDto(
    val metricId: String = "",
    val referenceDate: String? = null,
    val metricValue: Float? = null
) : Serializable {
    val formattedReferenceDate: LocalDate? = LocalDate.parse(
        referenceDate, DateTimeFormatter.ISO_DATE)
}
