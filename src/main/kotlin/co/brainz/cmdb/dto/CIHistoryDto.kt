/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIHistoryDto(
    val ciId: String? = "",
    var ciNo: String? = null,
    var ciStatus: String? = null,
    val instanceId: String? = null,
    val documentNo: String? = null,
    val createUser: String? = null,
    val automatic: Boolean,
    var applyDt: LocalDateTime? = LocalDateTime.now()
) : Serializable
