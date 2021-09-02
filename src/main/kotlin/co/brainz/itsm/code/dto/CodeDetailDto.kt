package co.brainz.itsm.code.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.io.Serializable

data class CodeDetailDto(
    @CheckUnacceptableCharInUrl
    var code: String = "",
    @CheckUnacceptableCharInUrl
    var pCode: String? = "",
    var codeName: String? = null,
    var codeValue: String? = null,
    var codeDesc: String? = null,
    var editable: Boolean? = true,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var codeLang: String? = null
) : Serializable
