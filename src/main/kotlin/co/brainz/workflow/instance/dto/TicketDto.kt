package co.brainz.workflow.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class TicketDto(
        val tokenId: String,
        val ticketId: String,
        val ticketName: String,
        val ticketDesc: String?,
        val createDt: LocalDateTime,
        val userKey: String
) : Serializable
