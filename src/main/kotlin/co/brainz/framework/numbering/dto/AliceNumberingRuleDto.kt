package co.brainz.framework.numbering.dto

import java.io.Serializable

data class AliceNumberingRuleDto(
    var numberingId: String = "",
    var numberingName: String = "",
    var numberingDesc: String? = null,
    var latestValue: String? = null
) : Serializable
