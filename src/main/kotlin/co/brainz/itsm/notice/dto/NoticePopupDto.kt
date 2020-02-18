package co.brainz.itsm.notice.dto

import java.io.Serializable

data class NoticePopupDto(
    var noticeTitle: String = "",
    var noticeContents: String = "",
    var createUserKey: String = ""
) : Serializable
