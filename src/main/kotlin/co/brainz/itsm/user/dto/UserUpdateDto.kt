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
    var officeNumber: String?,
    var mobileNumber: String?,
    var roles: Set<String>?,
    var certificationCode: String?,
    var status: String?,
    var timezone: String?,
    var lang: String?,
    var timeFormat: String?,
    var theme: String?,
    var fileSeq: Long?
) : Serializable
