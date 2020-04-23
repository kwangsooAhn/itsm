package co.brainz.itsm.code.dto

import co.brainz.itsm.code.entity.CodeEntity
import java.io.Serializable
import java.time.LocalDateTime

data class CodeDto (
        var code: String? = null,
        var pCode: CodeEntity? = null,
        var codeValue: String? = null,
        var editable: Boolean? = null,
        var createDt: LocalDateTime? = null,
        var createUserName: String? = null,
        var updateDt: LocalDateTime? = null,
        var updateUserName: String? = null
) : Serializable