package co.brainz.framework.numbering.dto

import java.io.Serializable
import java.time.LocalDateTime

data class AliceNumberingRuleDto(
    var numberingId: String = "",
    var numberingName: String = "",
    var numberingDesc: String? = null,
    var latestDate: LocalDateTime? = null,
    var latestValue: String? = null
) : Serializable
