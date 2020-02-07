package co.brainz.itsm.provider.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class ProcessDto(
        val processId: String,
        val processName: String,
        val processDesc: String? = null,
        val processStatus: String,
        val formId: String? = null,
        val formName: String? = null,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        var createDt: LocalDateTime?,
        val createUserkey: String?,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        var updateDt: LocalDateTime? = null,
        val updateUserkey: String? = null,
        val userName: String? = null,
        val enabled: Boolean? = false
) : Serializable
