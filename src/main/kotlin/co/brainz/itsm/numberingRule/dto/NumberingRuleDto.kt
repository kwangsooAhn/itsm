package co.brainz.itsm.numberingRule.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.io.Serializable
import java.time.LocalDateTime

data class NumberingRuleDto(
    var numberingId: String = "",
    @CheckUnacceptableCharInUrl
    var numberingName: String,
    var numberingDesc: String? = null,
    var latestDate: LocalDateTime? = null,
    var latestValue: String? = null,
    var patternList: MutableList<String>
) : Serializable

