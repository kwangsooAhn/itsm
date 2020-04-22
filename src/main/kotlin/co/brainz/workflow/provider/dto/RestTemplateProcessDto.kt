package co.brainz.workflow.provider.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateProcessDto(
    var processId: String = "",
    var processName: String = "",
    var processDesc: String? = null,
    var processStatus: String = "",
    var formId: String? = null,
    var formName: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    var createDt: LocalDateTime? = null,
    var createUserKey: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var userName: String? = null,
    var enabled: Boolean? = false
) : Serializable
