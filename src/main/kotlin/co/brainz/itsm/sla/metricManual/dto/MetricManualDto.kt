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
    val metricId: String = "",
    val metricName: String? = "",
    val referenceDt: LocalDate? = null,
    val metricValue: Double? = null,
    val metricUnitName: String? = null,
    val createDt: LocalDateTime? = null,
    val createUserName: String? = null
) : Serializable
