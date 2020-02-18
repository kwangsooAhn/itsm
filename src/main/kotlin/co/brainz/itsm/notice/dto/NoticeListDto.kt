package co.brainz.itsm.notice.dto

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime

data class NoticeListDto(
        var topNoticeYn: Boolean = true,
        var noticeNo: String = "",
        var noticeTitle: String = "",
        var popYn: Boolean = true,
        var createDt: LocalDateTime? = null,
        var aliceUserEntity: AliceUserEntity? = null
): Serializable
