/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartListReturnDto(
    val data: List<ChartListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable