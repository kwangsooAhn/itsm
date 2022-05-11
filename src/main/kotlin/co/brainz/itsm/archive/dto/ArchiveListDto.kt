/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ArchiveListDto(
    var archiveId: String = "",
    var archiveSeq: Long = 0,
    var archiveCategory: String = "",
    var archiveCategoryName: String = "",
    var archiveTitle: String = "",
    var views: Int = 0,
    var totalCount: Long = 0,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null
) : Serializable
