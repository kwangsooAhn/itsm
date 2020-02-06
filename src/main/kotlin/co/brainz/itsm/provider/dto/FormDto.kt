package co.brainz.itsm.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FormDto(
        val formId: String,
        val formName: String,
        val formStatus: String,
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        val createUserkey: String,
        var createDt: LocalDateTime,
        val updateUserkey: String? = null,
        var updateDt: LocalDateTime? = null,
        val userName: String? = null
) : Serializable
