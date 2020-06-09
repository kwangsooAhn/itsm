package co.brainz.itsm.code.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class CodeDetailDto(
    var code: String = "",
    var pCode: String? = "",
    var codeValue: String? = null,
    var editable: Boolean? = true,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null,
    var enabled: Boolean? = true
) : Serializable
