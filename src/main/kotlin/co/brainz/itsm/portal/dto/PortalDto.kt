package co.brainz.itsm.portal.dto

import java.io.Serializable
import java.time.LocalDateTime

data class PortalDto(
        var noticeTitle: String? = null,
        var noticeContents: String? = null,
        var faqTitle: String? = null,
        var faqContent: String? = null,
        var createDt: LocalDateTime? = null,
        var updateDt: LocalDateTime? = null
): Serializable