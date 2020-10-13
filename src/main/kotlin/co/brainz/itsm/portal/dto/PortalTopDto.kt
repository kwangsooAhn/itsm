package co.brainz.itsm.portal.dto

import java.io.Serializable
import java.time.LocalDateTime

data class PortalTopDto(
    val id: String = "",
    val title: String = "",
    val content: String = "",
    var createDt: LocalDateTime? = null
) : Serializable

