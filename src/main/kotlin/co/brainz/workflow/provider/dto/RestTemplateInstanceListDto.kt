package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateInstanceListDto(
    var instanceId: String?,
    var documentName: String?,
    var documentNo: String?,
    var createDt: LocalDateTime?,
    val tokenId: String? = null,
    val assigneeUserKey: String? = "",
    val assigneeUserName: String? = "",
    var related: Boolean = false
) : Serializable
