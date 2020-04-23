package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserListDto(
    var userKey: String,
    var userId: String,
    var userName: String
) : Serializable
