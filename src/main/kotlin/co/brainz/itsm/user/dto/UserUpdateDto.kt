package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserUpdateDto(
        var userKey: String,
        var userId: String,
        var userName: String?,
        var password: String?,
        var email: String?,
        var position: String?,
        var department: String?,
        var extensionNumber: String?,
        var phoneNumber: String?,
        var roles: Set<String>?,
        var certificationCode: String?,
        var status: String?,
        var timezone: String?,
        var lang: String?
) : Serializable
