package co.brainz.workflow.process.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfJsonProcessDto(
    var id: String = "",
    var name: String? = null,
    var description: String? = null,
    var status: String? = null,
    var formId: String? = null,
    var formName: String? = null,
    var createDt: LocalDateTime? = null,
    var createUserKey: String? = null,
    var updateDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var enabled: Boolean? = null
) : Serializable
