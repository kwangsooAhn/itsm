package co.brainz.itsm.customCode.dto

import co.brainz.itsm.customCode.constants.CustomCodeConstants
import java.time.LocalDateTime

data class CustomCodeDto(
    var customCodeId: String = "",
    var type: String = CustomCodeConstants.Type.TABLE.code,
    var customCodeName: String? = null,
    var targetTable: String? = null,
    var targetTableName: String? = null,
    var searchColumn: String? = null,
    var searchColumnName: String? = null,
    var valueColumn: String? = null,
    var valueColumnName: String? = null,
    var pCode: String? = null,
    var condition: String? = null,
    var sessionKey: String? = null,
    var createDt: LocalDateTime? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserName: String? = null,
    var enabled: Boolean = true
)
