/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto.average

import java.io.Serializable
import java.time.LocalDateTime

data class ChartTokenData(
    val instanceId: String,
    val instanceStartDt: LocalDateTime,
    val instanceEndDt: LocalDateTime,
    val tokenId: String,
    val value: String
) : Serializable
