package co.brainz.itsm.process.dto

import java.time.LocalDateTime

data class ProcessSearchDto(
        val processId: String,
        val processName: String,
        val processDesc: String? = null,
        val processStatus: String,
        val formName: String? = null,
        val createDt: LocalDateTime,
        val createUserkey: String,
        val updateDt: LocalDateTime? = null,
        val updateUserkey: String? = null,
        val enabled: Boolean
)