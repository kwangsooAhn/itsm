package co.brainz.workflow.engine.form.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfFormSaveDto(
        val formId: String,
        val formName: String,
        val formDesc: String? = null,
        val formStatus: String?,
        val updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null
) : Serializable
