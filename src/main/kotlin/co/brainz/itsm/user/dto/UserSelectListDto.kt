/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserSelectListDto(
    var userKey: String,
    var userId: String,
    var userName: String
) : Serializable
