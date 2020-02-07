package co.brainz.workflow.form.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class FormDto(
        var formId: String = "",
        val formName: String = "",
        val formStatus: String = "",
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        val createUserkey: String = "",
        val createDt: LocalDateTime = LocalDateTime.now(),
        val updateUserkey: String? = null,
        val updateDt: LocalDateTime? = null,
        val userName: String? = null
) : Serializable
