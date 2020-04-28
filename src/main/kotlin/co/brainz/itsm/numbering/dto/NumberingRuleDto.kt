package co.brainz.itsm.numbering.dto

import java.io.Serializable

data class NumberingRuleDto(
    var numberingId: String = "",
    var numberingName: String = "",
    var numberingDesc: String? = null,
    var latestValue: String? = null
) : Serializable
