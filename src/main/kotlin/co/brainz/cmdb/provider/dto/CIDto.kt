/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIDto(
    val ciId: String? = null,
    val ciNo: String? = null,
    val ciName: String? = null,
    val typeId: String? = null,
    val classId: String? = null,
    val ciIcon: String? = null,
    val ciDesc: String? = null,
    val automatic: Boolean? = false,
    val createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    val updateUserKey: String? = null,
    val updateDt: LocalDateTime? = null
) : Serializable
