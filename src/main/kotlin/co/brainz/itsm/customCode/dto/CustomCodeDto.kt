package co.brainz.itsm.customCode.dto

import java.io.Serializable
import java.time.LocalDateTime

data class CustomCodeDto(
        var customCodeId: String = "",
        var customCodeName: String = "",
        var targetTable: String = "",
        var keyColumn: String = "",
        var valueColumn: String = "",
        var createDt: LocalDateTime? = null,
        var createUserName: String? = null,
        var updateDt: LocalDateTime? = null,
        var updateUserName: String? = null,
        var enabled: Boolean = true
) : Serializable