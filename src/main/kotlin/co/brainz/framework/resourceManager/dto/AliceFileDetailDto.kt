/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.resourceManager.dto

import java.time.LocalDateTime
import java.io.Serializable

data class AliceFileDetailDto(
    var name: String,
    var fullpath: String,
    var extension: String,
    var size: String,
    var data: String? = null,
    var width: Int? = null,
    var height: Int? = null,
    var updateDt: LocalDateTime?
)

data class AliceFileDetailListReturnDto(
    val data: List<AliceFileDetailDto> = emptyList(),
    val totalCount: Long = 0,
    val totalCountWithoutCondition: Long = 0
) : Serializable
