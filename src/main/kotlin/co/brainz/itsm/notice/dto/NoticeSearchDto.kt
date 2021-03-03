package co.brainz.itsm.notice.dto

import java.io.Serializable

data class NoticeSearchDto(
    val searchValue: String,
    val fromDt: String,
    val toDt: String,
    val offset: Long = 0,
    var isScroll: Boolean = false
) : Serializable
