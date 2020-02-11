package co.brainz.itsm.notice.dto

import java.io.Serializable

data class NoticeSearchDto(
    val isSearch: Boolean,
    val searchValue: String,
    val fromDt: String,
    val toDt: String
) : Serializable