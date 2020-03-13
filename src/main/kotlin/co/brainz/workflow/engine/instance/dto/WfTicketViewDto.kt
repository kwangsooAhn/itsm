package co.brainz.workflow.engine.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfTicketViewDto(
        val ticketId: String,
        val ticketName: String,
        val ticketDesc: String? = null,
        val userKey: String,
        val createDt: LocalDateTime
) : Serializable
