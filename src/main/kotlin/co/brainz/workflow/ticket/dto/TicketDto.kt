package co.brainz.workflow.ticket.dto

import java.io.Serializable

data class TicketDto(
        val ticketId: String,
        val ticketName: String,
        val ticketDesc: String? = null
) : Serializable