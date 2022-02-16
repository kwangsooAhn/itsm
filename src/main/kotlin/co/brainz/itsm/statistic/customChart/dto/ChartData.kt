/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.dto

import java.io.Serializable

data class ChartData(
    val id: String,
    val category: String,
    val value: String,
    val series: String,
    val linkKey: String?
) : Serializable
