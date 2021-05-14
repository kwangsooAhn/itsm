/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.dto

import java.io.Serializable
import java.time.LocalDateTime

data class NoticePopupDto(
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var noticeContents: String = "",
    var createDt: LocalDateTime = LocalDateTime.now(),
    var createUserKey: String = "",
    var createUserName: String = ""
) : Serializable
