package co.brainz.itsm.code.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CodeDto (
    var code: String = "",
    var pCode: String? = null,
    var codeValue: String? = null,
    var editable: Boolean? = true,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null
): Serializable