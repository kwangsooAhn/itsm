package co.brainz.itsm.user.dto

import java.io.Serializable
import java.time.LocalDateTime

class UserListExcelDto(
    var userId: String = "",
    var userName: String = "",
    var email: String = "",
    var position: String? = null,
    var department: String? = null,
    var officeNumber: String? = null,
    var mobileNumber: String? = null,
    var createDt: LocalDateTime? = null,
    var absenceYn: Boolean = false
) : Serializable