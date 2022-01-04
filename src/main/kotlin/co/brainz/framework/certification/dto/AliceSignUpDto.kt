/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.certification.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.io.Serializable

data class AliceSignUpDto(
    @CheckUnacceptableCharInUrl
    var userId: String,
    var userName: String,
    var password: String?,
    var email: String,
    var position: String?,
    var groupId: String?,
    var officeNumber: String?,
    var mobileNumber: String?,
    var roles: Set<String>?,
    var timezone: String?,
    var lang: String?,
    var timeFormat: String?,
    var theme: String?,
    var fileSeq: Long?,
    var avatarUUID: String
) : Serializable
