/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricManualDto(
    val metric: String = "",
    val metricName: String? = null,
    val referenceDt: LocalDateTime? = null,
    val metricValue: Long? = null,
    val metricUnitName: String? = null,
    val userName: String,
    val createDt: LocalDateTime? = null
) : Serializable
