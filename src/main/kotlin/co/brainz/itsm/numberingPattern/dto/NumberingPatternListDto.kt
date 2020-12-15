package co.brainz.itsm.numberingPattern.dto

import java.io.Serializable

data class NumberingPatternListDto (
    var patternId: String,
    var patternName: String,
    var patternType: String,
    var patternValue: String
) : Serializable
