package co.brainz.workflow.engine.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class WfInstanceViewDto(
    var tokenId: String = "",
    var instanceId: String = "",
    var documentName: String = "",
    var documentDesc: String? = null,
    var createDt: LocalDateTime = LocalDateTime.now(),
    var userKey: String = ""
) : Serializable
