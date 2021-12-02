/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto.percent

import java.io.Serializable

data class ChartCategoryTag(
    val category: String,
    val totalCount: Int = 0,
    val tagCountList: List<ChartTagCount>
) : Serializable
