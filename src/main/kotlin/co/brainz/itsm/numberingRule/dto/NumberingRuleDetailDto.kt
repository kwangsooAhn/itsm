/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.dto

import java.io.Serializable
import java.time.LocalDateTime

data class NumberingRuleDetailDto(
    var numberingId: String,
    var numberingName: String,
    var numberingDesc: String? = null,
    var latestDate: LocalDateTime? = null,
    var latestValue: String? = null,
    var patternList: MutableList<NumberingPatternMapDto>,
    var editable: Boolean? = null
) : Serializable

data class NumberingPatternMapDto(
    var patternId: String,
    var patternName: String,
    var patternType: String,
    var patternValue: String,
    var patternOrder: Int
) : Serializable
