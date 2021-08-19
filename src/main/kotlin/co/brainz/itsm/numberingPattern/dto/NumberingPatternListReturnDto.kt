/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingPattern.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class NumberingPatternListReturnDto(
    val data: List<NumberingPatternListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
