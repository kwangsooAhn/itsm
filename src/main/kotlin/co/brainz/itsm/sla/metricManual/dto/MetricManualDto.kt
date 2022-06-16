/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

data class MetricManualDto(
    val metricManualId: String = "",
    val metricName: String? = "",
    val referenceDate: LocalDate? = null,
    val metricValue: Float? = null,
    val metricUnit: String? = null,
    val createDt: LocalDateTime? = null,
    val createUserName: String? = null
) : Serializable
