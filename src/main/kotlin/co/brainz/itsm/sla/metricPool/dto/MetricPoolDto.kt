/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import java.io.Serializable

data class MetricPoolDto(
    val metricId: String,
    val metricName: String,
    val metricDesc: String? = null,
    val metricGroupName: String,
    val metricTypeName: String,
    val metricUnitName: String,
    val calculationTypeName: String
) : Serializable
