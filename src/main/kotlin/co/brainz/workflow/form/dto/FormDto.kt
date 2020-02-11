package co.brainz.workflow.form.dto

import java.io.Serializable
import java.time.LocalDateTime

data class FormDto(
        val formId: String,
        val formName: String,
        val formStatus: String,
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        val createUserKey: String,
        val createDt: LocalDateTime,
        val updateUserKey: String? = null,
        val updateDt: LocalDateTime? = null,
        val userName: String? = null
) : Serializable
