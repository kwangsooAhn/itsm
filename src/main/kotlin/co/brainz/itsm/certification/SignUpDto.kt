package co.brainz.itsm.certification

import java.io.Serializable

data class SignUpDto (
        var userId: String,
        var userName: String,
        var email: String,
        var position: String?,
        var department: String?,
        var extensionNumber: String?,
        var phoneNumber: String?
): Serializable
