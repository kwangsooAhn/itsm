/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserCustomDto(
    var userKey: String = "",
    var customType: String = "",
    var customValue: String = ""
) : Serializable
