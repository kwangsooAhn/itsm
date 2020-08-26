/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable
import java.time.LocalDateTime

class UserDto(
    var userKey: String = "",
    var userId: String = "",
    var userName: String = "",
    var email: String = "",
    var useYn: Boolean = true,
    var tryLoginCount: Int = 0,
    var position: String? = null,
    var department: String? = null,
    var officeNumber: String? = null,
    var mobileNumber: String? = null,
    var status: String? = null,
    var certificationCode: String? = null,
    var platform: String? = null,
    var expiredDt: LocalDateTime? = null,
    var oauthKey: String? = null,
    var timezone: String? = null,
    var lang: String? = null,
    var timeFormat: String? = null,
    var theme: String? = null,
    var avatarId: String = "",
    var avatarPath: String = "",
    var avatarValue: String = "",
    var avatarSize: Long = 0L,
    var createDt: LocalDateTime? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
