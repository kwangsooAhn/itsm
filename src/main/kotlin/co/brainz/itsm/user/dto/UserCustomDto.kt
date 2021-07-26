package co.brainz.itsm.user.dto

import co.brainz.itsm.user.constants.UserConstants

import java.io.Serializable

data class UserCustomDto(
    var userKey: String = "",
    var customType: String = UserConstants.UserCustom.COLOR.code,
    var customValue: String
) : Serializable
