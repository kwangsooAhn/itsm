package co.brainz.itsm.portal.dto

import java.io.Serializable
import java.time.LocalDateTime

data class PortalDto(
    var portalTitle: String? = null,
    var portalContent: String? = null,
    var createDt: LocalDateTime? = null,
    var updateDt: LocalDateTime? = null,
    var tableName: String? = null
) : Serializable
