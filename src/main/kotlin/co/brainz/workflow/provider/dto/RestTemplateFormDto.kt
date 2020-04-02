package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateFormDto(
        var id: String = "",
        var name: String = "",
        var status: String = "",
        var desc: String? = null,
        var editable: Boolean = false,
        var createUserKey: String? = null,
        var createDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        var updateDt: LocalDateTime? = null
) : Serializable

