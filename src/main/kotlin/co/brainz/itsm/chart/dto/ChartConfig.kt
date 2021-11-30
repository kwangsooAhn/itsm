/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartConfig(
    var range: ChartRange,
    var operation: String = "",
    var periodUnit: String? = null
) : Serializable
