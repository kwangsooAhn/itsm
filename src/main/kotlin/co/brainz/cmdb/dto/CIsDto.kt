/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIsDto(
    val ciId: String = "",
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciStatus: String? = null,
    var typeId: String? = null,
    var typeName: String? = null,
    var classId: String? = null,
    var className: String? = null,
    var ciIcon: String? = null,
    var ciDesc: String? = null,
    var interlink: Boolean? = false,
    val createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    val updateUserKey: String? = null,
    val updateDt: LocalDateTime? = null
) : Serializable
