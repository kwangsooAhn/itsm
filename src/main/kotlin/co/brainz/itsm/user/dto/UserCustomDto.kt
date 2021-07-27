/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.user.dto

import co.brainz.itsm.user.constants.UserConstants
import java.io.Serializable

data class UserCustomDto(
    var userKey: String = "",
    var customType: String = UserConstants.UserCustom.COLOR.code,
    var customValue: String
) : Serializable
