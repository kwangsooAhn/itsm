package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserUpdateDto(
        var userId: String,
        var userName: String?,
        var email: String?,
        var position: String?,
        var department: String?,
        var extensionNumber: String?,
        var phoneNumber: String?,
        var roles: Set<String>?,
        var certificationCode: String?,
        var status: String?
) : Serializable
