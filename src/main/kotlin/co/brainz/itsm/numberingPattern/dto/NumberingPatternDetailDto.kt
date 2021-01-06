package co.brainz.itsm.numberingPattern.dto

import java.io.Serializable

data class NumberingPatternDetailDto(
    var patternId: String,
    var patternName: String,
    var patternType: String,
    var patternValue: String,
    var editable: Boolean? = null
) : Serializable
