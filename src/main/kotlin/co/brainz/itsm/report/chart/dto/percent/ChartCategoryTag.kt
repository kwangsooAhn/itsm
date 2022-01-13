/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.chart.dto.percent

import java.io.Serializable

data class ChartCategoryTag(
    val category: String,
    val totalCount: Int = 0,
    val tagCountList: List<co.brainz.itsm.report.chart.dto.percent.ChartTagCount>
) : Serializable
