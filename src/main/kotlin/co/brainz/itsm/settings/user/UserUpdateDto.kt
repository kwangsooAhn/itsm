package co.brainz.itsm.settings.user

import java.io.Serializable

data class UserUpdateDto(
        var userId: String,
        var userName: String?,
        var position: String?,
        var department: String?,
        var extensionNumber: String?
) : Serializable