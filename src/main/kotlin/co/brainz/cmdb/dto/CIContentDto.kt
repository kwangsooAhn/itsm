/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.dto

import java.io.Serializable

data class CIContentDto(
    val key: String,
    val value: ArrayList<Any?> = arrayListOf()
) : Serializable
