package co.brainz.itsm.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateFormDto(
        var formId: String = "",
        var formName: String = "",
        var formStatus: String = "",
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        var createUserKey: String? = null,
        var createDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null,
        val userName: String? = null
) : Serializable

