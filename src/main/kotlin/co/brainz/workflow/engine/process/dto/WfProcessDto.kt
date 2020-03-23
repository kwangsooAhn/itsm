package co.brainz.workflow.engine.process.dto

import com.fasterxml.jackson.annotation.JsonFormat
import java.io.Serializable
import java.time.LocalDateTime

data class WfProcessDto(
    var id: String = "",
    var name: String? = null,
    var description: String? = null,
    var status: String? = null,
    var formId: String? = null,
    var formName: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    var createDt: LocalDateTime? = null,
    var createUserKey: String? = null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var enabled: Boolean? = null
) : Serializable
