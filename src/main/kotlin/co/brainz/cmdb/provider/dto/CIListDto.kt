package co.brainz.cmdb.provider.dto
/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

import java.io.Serializable
import java.time.LocalDateTime

data class CIListDto(
    val ciId: String? = "",
    var ciNo: String? = null,
    var ciName: String? = null,
    var ciStatus: String? = null,
    var typeId: String? = null,
    var typeName: String? = null,
    var classId: String? = null,
    var className: String? = null,
    var ciIcon: String? = null,
    var ciDesc: String? = null,
    var automatic: Boolean? = false,
    val createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    val updateUserKey: String? = null,
    val updateDt: LocalDateTime? = null,
    var tags: MutableList<CITagDto>? = null,
    var totalCount: Long = 0
) : Serializable
