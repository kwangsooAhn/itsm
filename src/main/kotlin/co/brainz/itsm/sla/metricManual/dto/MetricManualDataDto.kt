/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDate

data class MetricManualDataDto(
    val metricId: String = "",
    val referenceDate: LocalDate? = null,
    val metricValue: Double? = null
) : Serializable
