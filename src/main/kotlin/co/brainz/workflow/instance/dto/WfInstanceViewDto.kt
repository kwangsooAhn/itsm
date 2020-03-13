package co.brainz.workflow.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceViewDto(
        val tokenId: String,
        val instanceId: String,
        val documentName: String,
        val documentDesc: String?,
        val createDt: LocalDateTime,
        val userKey: String
) : Serializable
