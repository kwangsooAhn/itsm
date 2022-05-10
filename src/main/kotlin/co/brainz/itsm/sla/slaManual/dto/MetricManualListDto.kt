/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.slaManual.dto

import java.io.Serializable
import java.time.LocalDateTime

data class MetricManualListDto(
    val metricId: String,
    val metricName: String? = null,
    val referenceDt: LocalDateTime? = null,
    val metricValue: String? = null,
    val metricUnit: String? = null,
    val createDt: LocalDateTime? = null,
    val createUserKey: String
) : Serializable
