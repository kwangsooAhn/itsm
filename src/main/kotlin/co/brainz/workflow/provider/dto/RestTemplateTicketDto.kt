package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class RestTemplateTicketDto(
        val tokenId: String,
        val ticketId: String,
        val ticketName: String,
        val ticketDesc: String? = null,
        val userKey: String,
        var createDt: LocalDateTime
) : Serializable
