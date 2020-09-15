package co.brainz.itsm.code.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CodeDetailDto(
    var code: String = "",
    var pCode: String? = "",
    var codeName: String? = null,
    var codeValue: String? = null,
    var codeDesc: String? = null,
    var editable: Boolean? = true,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null,
    var enabled: Boolean? = true,
    var level: Int? = null
) : Serializable
