package co.brainz.workflow.engine.form.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfFormDto(
        var formId: String = "",
        var formName: String = "",
        var formStatus: String? = "",
        var formDesc: String? = null,
        var formEnabled: Boolean = false,
        var createUserKey: String? = null,
        var createDt: LocalDateTime? = null,
        var updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null
) : Serializable
