package co.brainz.itsm.numberingRule.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.io.Serializable
import java.time.LocalDateTime

data class NumberingRuleDto (
    @CheckUnacceptableCharInUrl
    var numberingId: String,
    var numberingName: String,
    var numberingDesc: String,
    var latestDate: LocalDateTime,
    var latestValue: String
) : Serializable

