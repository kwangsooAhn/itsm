/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.io.Serializable

data class UserUpdateDto(
    var userKey: String,
    @CheckUnacceptableCharInUrl
    var userId: String,
    @CheckUnacceptableCharInUrl
    var userName: String?,
    var password: String?,
    var email: String?,
    @CheckUnacceptableCharInUrl
    var position: String?,
    var department: String?,
    var officeNumber: String?,
    var mobileNumber: String?,
    var roles: Set<String>?,
    var certificationCode: String?,
    var status: String?,
    var timezone: String?,
    var lang: String?,
    var timeFormat: String?,
    var theme: String?,
    var fileSeq: Long?,
    var useYn: Boolean?,
    var avatarUUID: String,
    var absenceYn: Boolean,
    var absence: UserAbsenceDto?
) : Serializable
