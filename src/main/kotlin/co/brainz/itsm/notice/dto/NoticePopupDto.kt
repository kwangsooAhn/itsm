package co.brainz.itsm.notice.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime

data class NoticePopupDto(
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var noticeContents: String = "",
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserKey: String = "",
    var aliceUserEntity: AliceUserEntity? = null
) : Serializable
