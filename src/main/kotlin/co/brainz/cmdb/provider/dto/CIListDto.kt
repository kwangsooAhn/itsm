package co.brainz.cmdb.provider.dto
/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */


import java.io.Serializable

data class CIListDto(
    val ciId: String? = null,
    val ciNo: String? = null,
    val ciName: String? = null,
    val typeId: String? = null,
    var typeName: String? = null,
    val ciIcon: String? = null,
    val ciDesc: String? = null,
    val classId: String? = null,
    val automatic: String? = null,
    val createUserKey: String? = null,
    val createDt: String? = null,
    val updateUserKey: String? = null,
    val updateDt: String? = null,
    var totalCount: Long = 0
) : Serializable
