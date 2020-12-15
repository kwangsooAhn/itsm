package co.brainz.itsm.numberingPattern.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl
import java.io.Serializable

data class NumberingPatternDto (
    @CheckUnacceptableCharInUrl
    var patternId: String = "",
    var patternName: String,
    var patternType: String = "",
    var patternValue: String
) : Serializable
