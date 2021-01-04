package co.brainz.itsm.numberingRule.dto

import co.brainz.itsm.numberingPattern.dto.NumberingPatternDetailDto
import java.io.Serializable
import java.time.LocalDateTime

data class NumberingRuleDetailDto(
    var numberingId: String,
    var numberingName: String,
    var numberingDesc: String? = null,
    var latestDate: LocalDateTime? = null,
    var latestValue: String? = null,
    var patternList: MutableList<NumberingPatternDetailDto>
) : Serializable

