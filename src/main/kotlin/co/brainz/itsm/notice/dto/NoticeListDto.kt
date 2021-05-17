/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.dto

import java.io.Serializable
import java.time.LocalDateTime

data class NoticeListDto(
    var topNoticeYn: Boolean = true,
    var noticeNo: String = "",
    var noticeTitle: String = "",
    var popYn: Boolean = true,
    var createDt: LocalDateTime? = null,
    var popStrtDt: LocalDateTime? = null,
    var popEndDt: LocalDateTime? = null,
    var popWidth: Int? = null,
    var popHeight: Int? = null,
    var topNoticeStrtDt: LocalDateTime? = null,
    var topNoticeEndDt: LocalDateTime? = null,
    var createUserName: String? = ""
) : Serializable
