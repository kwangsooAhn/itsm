package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateInstanceListDto(
    var instanceId: String?,
    var documentName: String?,
    var documentNo: String?,
    var instanceStartDt: LocalDateTime?,
    var instanceEndDt: LocalDateTime?,
    var instanceCreateUserKey: String?,
    var instanceCreateUserName: String?
) : Serializable
