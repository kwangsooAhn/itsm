/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CICapacityDto(
    val ciId: String,
    val referenceDt: LocalDateTime,
    val memAvg: Double? = 0.0,
    val cpuAvg: Double? = 0.0,
    val diskAvg: Double? = 0.0,
    val mappingId: String? = ""
):Serializable
