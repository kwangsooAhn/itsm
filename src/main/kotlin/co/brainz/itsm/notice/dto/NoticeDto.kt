package co.brainz.itsm.notice.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime

data class NoticeDto(
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var noticeContents: String = "",
    var popYn: Boolean = true,
    var popStrtDt: LocalDateTime? = null,
    var popEndDt: LocalDateTime? = null,
    var popWidth: Int? = 500,
    var popHeight: Int? = 500,
    var topNoticeYn: Boolean = true,
    var topNoticeStrtDt: LocalDateTime? = null,
    var topNoticeEndDt: LocalDateTime? = null,
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var aliceUserEntity: AliceUserEntity? = null,
    var fileSeq: List<Long>? = null
) : Serializable
