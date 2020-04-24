package co.brainz.itsm.notice.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class NoticeDto(
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var noticeContents: String = "",
    var popYn: Boolean = true,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    var popStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    var popEndDt: LocalDateTime? = null,
    var popWidth: Int? = 500,
    var popHeight: Int? = 500,
    var topNoticeYn: Boolean = true,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    var topNoticeStrtDt: LocalDateTime? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    var topNoticeEndDt: LocalDateTime? = null,
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var aliceUserEntity: AliceUserEntity? = null,
    var fileSeq: List<Long>? = null
) : Serializable
