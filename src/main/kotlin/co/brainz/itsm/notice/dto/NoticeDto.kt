package co.brainz.itsm.notice.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class NoticeDto(
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var noticeContents: String = "",
    var popYn: Boolean = true,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    var popStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    var popEndDt: LocalDateTime? = null,
    var popWidth: Int? = 500,
    var popHeight: Int? = 500,
    var topNoticeYn: Boolean = true,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    var topNoticeStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    var topNoticeEndDt: LocalDateTime? = null,
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserkey: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserkey: String? = null,
    var fileSeq: List<Long>?
) : Serializable
