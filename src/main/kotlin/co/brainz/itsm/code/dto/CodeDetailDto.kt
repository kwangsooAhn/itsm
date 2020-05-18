package co.brainz.itsm.code.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CodeDetailDto(
    var code: String = "",
    var pCode: String? = "",
    var codeValue: String? = null,
    var editable: Boolean? = true,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null,
    var enabled: Boolean? = true
) : Serializable