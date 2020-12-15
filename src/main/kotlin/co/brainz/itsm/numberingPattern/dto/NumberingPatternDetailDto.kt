package co.brainz.itsm.numberingPattern.dto

import java.io.Serializable

data class NumberingPatternDetailDto (
    var patternId: String,
    var PatternName: String,
    var patternType: String,
    var patternValue: String
) : Serializable
