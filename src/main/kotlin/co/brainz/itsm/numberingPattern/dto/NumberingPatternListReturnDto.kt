package co.brainz.itsm.numberingPattern.dto

import java.io.Serializable

data class NumberingPatternListReturnDto(
    val data: List<NumberingPatternListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
