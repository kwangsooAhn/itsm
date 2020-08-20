/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.dto

import java.io.Serializable
import java.time.LocalDateTime

data class DownloadListDto(
    var downloadId: String = "",
    var downloadSeq: Long = 0,
    var downloadCategory: String = "",
    var downloadTitle: String = "",
    var views: Int = 0,
    var totalCount: Long = 0,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null
) : Serializable
