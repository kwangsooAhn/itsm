/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ArchiveDto(
    var archiveId: String = "",
    var archiveSeq: Long = 0,
    var archiveCategory: String = "",
    var archiveCategoryName: String = "",
    var archiveTitle: String = "",
    var fileSeqList: List<Long>? = null,
    var delFileSeqList: List<Long>? = null,
    var views: Int = 0,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null
) : Serializable
