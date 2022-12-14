/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.dto

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
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var fileSeq: List<Long>? = null,
    var delFileSeq: List<Long>? = null
) : Serializable
