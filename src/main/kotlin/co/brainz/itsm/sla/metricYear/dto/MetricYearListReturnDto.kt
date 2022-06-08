/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class MetricYearListReturnDto(
    val data: List<MetricYearDataDto> = emptyList(),
    val totalCount: Long? = 0L,
    val totalCountWithoutCondition: Long? = 0L
): Serializable
