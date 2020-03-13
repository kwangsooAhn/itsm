package co.brainz.workflow.form.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfFormDto(
        var formId: String = "",
        var formName: String = "",
        val formStatus: String? = "",
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        val createUserKey: String? = null,
        val createDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        val updateDt: LocalDateTime? = null
) : Serializable
