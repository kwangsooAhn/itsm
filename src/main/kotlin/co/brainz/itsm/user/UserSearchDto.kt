package co.brainz.itsm.user

import java.io.Serializable

data class UserSearchDto(
        val searchKey: String,
        val searchValue: String
): Serializable