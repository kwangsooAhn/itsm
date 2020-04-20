package co.brainz.workflow.engine.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceHistoryDto(
    val tokenStartDt: LocalDateTime?,
    val tokenEndDt: LocalDateTime?,
    val elementName: String?,
    val elementType: String?,
    val assigneeId: String?,
    val assigneeName: String?
) : Serializable {
}
