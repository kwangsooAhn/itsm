package co.brainz.framework.certification.dto

import java.io.Serializable

data class SignUpDto(
        var userId: String,
        var userName: String,
        var password: String,
        var email: String,
        var position: String?,
        var department: String?,
        var officeNumber: String?,
        var mobileNumber: String?
) : Serializable
