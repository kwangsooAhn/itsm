package co.brainz.workflow.instance.dto

import co.brainz.workflow.element.entity.WfElementEntity
import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceListTokenDto(
    val tokenId: String,
    val element: WfElementEntity,
    val assigneeId: String? = null,
    val tokenStartDt: LocalDateTime? = null,
    val tokenEndDt: LocalDateTime? = null
) : Serializable
