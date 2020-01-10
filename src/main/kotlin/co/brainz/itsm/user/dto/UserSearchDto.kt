package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserSearchDto(
        val searchKey: MutableList<String>,
        val searchValue: String
): Serializable
