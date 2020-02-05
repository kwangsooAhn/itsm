package co.brainz.workflow.form.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FormDto(
        val formId: String,
        val formName: String,
        val formStatus: String,
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        val createUserkey: String,
        val createDt: LocalDateTime,
        val updateUserkey: String? = null,
        val updateDt: LocalDateTime? = null
) : Serializable
