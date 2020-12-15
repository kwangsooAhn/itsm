package co.brainz.itsm.numberingRule.dto

import java.io.Serializable
import java.time.LocalDateTime

data class NumberingRuleDetailDto (
    var numberingId: String,
    var numberingName: String,
    var numberingDesc: String,
    var latestDate: LocalDateTime,
    var latestValue: String
) : Serializable

