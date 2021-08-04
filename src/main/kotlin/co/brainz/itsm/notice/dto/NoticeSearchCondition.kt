package co.brainz.itsm.notice.dto

import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NoticeSearchCondition(
    val searchValue: String,
    val fromDt: String,
    val toDt: String,
    val pageNum: Long = 1L
) : Serializable {
    val formattedFromDt: LocalDateTime = LocalDateTime.parse(fromDt, DateTimeFormatter.ISO_DATE_TIME)
    val formattedToDt: LocalDateTime = LocalDateTime.parse(toDt, DateTimeFormatter.ISO_DATE_TIME)
}
