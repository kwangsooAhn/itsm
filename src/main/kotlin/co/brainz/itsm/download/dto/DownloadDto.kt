package co.brainz.itsm.download.dto

import java.io.Serializable
import java.time.LocalDateTime

data class DownloadDto(
        var downloadId: String = "",
        var downloadSeq: Long = 0,
        var downloadCategory: String = "",
        var downloadTitle: String = "",
        var fileSeqList: List<Long>? = null,
        var views: Long = 0,
        var createDt: LocalDateTime? = null,
        var createUserName: String? = null,
        var updateDt: LocalDateTime? = null,
        var updateUserName: String? = null
): Serializable