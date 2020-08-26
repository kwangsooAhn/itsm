package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateProcessDto(
    var processId: String = "",
    var processName: String = "",
    var processDesc: String? = null,
    var processStatus: String = "",
    var formId: String? = null,
    var formName: String? = null,
    var createDt: LocalDateTime? = null,
    var createUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var userName: String? = null,
    var enabled: Boolean? = false
) : Serializable
