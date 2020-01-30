package co.brainz.workflow.process

import java.time.LocalDateTime

data class ProcessDto(
    val procId: String,
    val procName: String,
    val procDesc: String? = null,
    val procStatus: String,
    val formId: String? = null,
    val formName: String? = null,
    val createDt: LocalDateTime?,
    val createUserkey: String?,
    val updateDt: LocalDateTime? = null,
    val updateUserkey: String? = null,
    val enabled: Boolean? = false
)
