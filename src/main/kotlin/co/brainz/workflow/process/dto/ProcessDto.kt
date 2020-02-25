package co.brainz.workflow.process.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class ProcessDto(
        val processId: String,
        val processName: String,
        val processDesc: String? = null,
        var processStatus: String,
        val formId: String? = null,
        val formName: String? = null,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        var createDt: LocalDateTime?,
        val createUserKey: String?,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        var updateDt: LocalDateTime? = null,
        val updateUserKey: String? = null,
        val enabled: Boolean? = false
) : Serializable
