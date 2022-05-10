/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.fileTransaction.dto

import java.time.LocalDateTime

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
