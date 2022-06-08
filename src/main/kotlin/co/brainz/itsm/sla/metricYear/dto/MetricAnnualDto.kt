/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricAnnualDto(
    val metricId: String = "",
    val metricGroupName: String? = "",
    val metricName: String? = "",
    val calculationType: String? = "",
    val zqlString: String = "",
    val metricType: String ="",
    val minValue: Float? = 0f,
    val maxValue: Float? = 0f,
    val weightValue: Float = 0f,
    var score: Float? = 0f,
    val owner: String? = "",
    val comment: String? = ""
) : Serializable
