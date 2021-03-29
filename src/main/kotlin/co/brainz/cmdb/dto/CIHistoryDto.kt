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
    // 임시 field
    val documentNo: String? = "TEST-20210401-000",
    val createUser: String? = "Admin",
    var applyDt: LocalDateTime? = LocalDateTime.now()
) : Serializable
