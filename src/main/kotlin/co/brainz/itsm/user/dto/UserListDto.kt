/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable
import java.time.LocalDateTime

data class UserListDto(
    var userKey: String = "",
    var userId: String = "",
    var userName: String = "",
    var email: String = "",
    var position: String? = null,
    var department: String? = null,
    var officeNumber: String? = null,
    var mobileNumber: String? = null,
    var avatarPath: String = "",
    var avatarType: String = "",
    var avatarValue: String = "",
    var uploaded: Boolean = false,
    var uploadedLocation: String = "",
    var createDt: LocalDateTime? = null
) : Serializable
