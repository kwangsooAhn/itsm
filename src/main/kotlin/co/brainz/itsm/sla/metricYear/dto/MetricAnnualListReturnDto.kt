/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import java.io.Serializable

data class MetricAnnualListReturnDto(
    val data: List<MetricAnnualDto> = emptyList(),
    val totalCount: Long = 0,
    val totalCountWithoutCondition: Long = 0
): Serializable
