package co.brainz.itsm.notice.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime

data class NoticeQueryResultDto(
    var topNoticeYn: Boolean = true,
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var popYn: Boolean = true,
    var popWidth: Int? = null,
    var popHeight: Int? = null,
    var createDt: LocalDateTime? = null,
    var popStrtDt: LocalDateTime? = null,
    var popEndDt: LocalDateTime? = null,
    var topNoticeStrtDt: LocalDateTime? = null,
    var topNoticeEndDt: LocalDateTime? = null,
    var aliceUserEntity: AliceUserEntity? = null
) : Serializable
