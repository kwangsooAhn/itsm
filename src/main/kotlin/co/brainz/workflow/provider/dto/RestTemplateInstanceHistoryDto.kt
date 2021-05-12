package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateInstanceHistoryDto(
    var tokenStartDt: LocalDateTime?,
    var tokenEndDt: LocalDateTime?,
    val elementName: String?,
    val elementType: String?,
    val tokenStatus: String?,
    val tokenAction: String?,
    val assigneeId: String?,
    val assigneeName: String?
) : Serializable
