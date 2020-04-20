package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateRelatedInstanceDto(
    val instanceId: String?,
    val documentName: String?,
    var instanceStartDt: LocalDateTime?,
    var instanceEndDt: LocalDateTime?,
    val instanceCreateUserKey: String?,
    val instanceCreateUserName: String?
) : Serializable {
}