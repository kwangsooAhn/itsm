package co.brainz.itsm.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class TicketDto(
        val tokenId: String,
        val ticketId: String,
        val ticketName: String,
        val ticketDesc: String? = null,
        val userKey: String,
        var createDt: LocalDateTime
) : Serializable
