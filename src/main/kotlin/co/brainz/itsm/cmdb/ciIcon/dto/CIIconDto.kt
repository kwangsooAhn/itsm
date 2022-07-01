/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CIIconDto(
    var name: String,
    var fullpath: String,
    var extension: String,
    var editable: Boolean = true,
    var size: String,
    var data: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var updateDt: LocalDateTime?
) : Serializable

data class CIIconListReturnDto(
    val data: List<CIIconDto> = emptyList(),
    val totalCount: Long = 0,
    val totalCountWithoutCondition: Long = 0
) : Serializable
