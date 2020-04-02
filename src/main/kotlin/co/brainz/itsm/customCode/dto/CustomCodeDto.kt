package co.brainz.itsm.customCode.dto

import java.time.LocalDateTime

data class CustomCodeDto(
        var customCodeId: String = "",
        var customCodeName: String = "",
        var targetTable: String = "",
        var targetTableName: String = "",
        var searchColumn: String = "",
        var searchColumnName: String = "",
        var valueColumn: String = "",
        var valueColumnName: String = "",
        var createDt: LocalDateTime? = null,
        var createUserName: String? = null,
        var updateDt: LocalDateTime? = null,
        var updateUserName: String? = null,
        var enabled: Boolean = true
)