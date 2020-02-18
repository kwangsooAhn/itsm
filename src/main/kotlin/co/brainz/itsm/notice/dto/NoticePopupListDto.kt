package co.brainz.itsm.notice.dto

import java.io.Serializable

data class NoticePopupListDto(
    var noticeNo: String = "",
    var popWidth: Int = 500,
    var popHeight: Int = 500
) : Serializable
