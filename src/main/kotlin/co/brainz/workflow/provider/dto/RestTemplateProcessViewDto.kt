package co.brainz.workflow.provider.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateProcessViewDto(
    var id: String = "",
    var name: String? = null,
    var description: String? = null,
    var status: String? = null,
    var createDt: LocalDateTime? = null,
    var createUserKey: String? = null,
    var createUserName: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateUserName: String? = null,
    var enabled: Boolean? = null,
    var totalCount: Long = 0
) : Serializable
