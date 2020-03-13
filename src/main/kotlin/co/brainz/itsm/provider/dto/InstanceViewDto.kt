package co.brainz.itsm.provider.dto

import java.io.Serializable
import java.time.LocalDateTime

data class InstanceViewDto(
        val tokenId: String,
        val instanceId: String,
        val documentName: String,
        val documentDesc: String? = null,
        var createDt: LocalDateTime,
        val userKey: String
) : Serializable
