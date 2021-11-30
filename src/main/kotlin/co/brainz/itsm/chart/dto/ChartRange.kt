/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ChartRange(
    var type: String,
    var from: LocalDateTime? = null,
    var to: LocalDateTime? = null
) : Serializable
