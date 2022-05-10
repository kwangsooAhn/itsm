/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricManual.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricManualDto(
    val metricId: String = "",
    val metricName: String? = null,
    val referenceDt: LocalDateTime? = null,
    val metricValue: Long? = null,
    val metricUnitName: String? = null,
    val createDt: LocalDateTime? = null,
    val createUserName: String
) : Serializable
