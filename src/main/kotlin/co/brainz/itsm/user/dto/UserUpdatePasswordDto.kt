/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserUpdatePasswordDto(
    var userId: String?,
    var nowPassword: String?,
    var newPassword: String?
) : Serializable
