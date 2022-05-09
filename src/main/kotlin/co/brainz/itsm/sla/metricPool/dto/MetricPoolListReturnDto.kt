/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class MetricPoolListReturnDto(
    val data: List<MetricPoolListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
