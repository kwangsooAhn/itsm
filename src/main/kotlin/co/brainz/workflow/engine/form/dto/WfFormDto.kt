package co.brainz.workflow.engine.form.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfFormDto(
        var id: String = "",
        var name: String = "",
        var status: String? = "",
        var desc: String? = null,
        var editable: Boolean = false,
        var createUserKey: String? = null,
        var createDt: LocalDateTime? = null,
        var updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null
) : Serializable
