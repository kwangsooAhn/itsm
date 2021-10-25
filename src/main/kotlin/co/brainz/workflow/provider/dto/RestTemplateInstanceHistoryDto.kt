package co.brainz.workflow.provider.dto

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateInstanceHistoryDto(
    var tokenStartDt: LocalDateTime?,
    var tokenEndDt: LocalDateTime?,
    val elementName: String?,
    val elementType: String?,
    val tokenStatus: String?,
    @JsonSetter(nulls= Nulls.AS_EMPTY)
    val tokenAction: String?,
    val assigneeId: String?,
    val assigneeName: String?
) : Serializable
