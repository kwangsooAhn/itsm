/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.numberingRule.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class NumberingRuleListReturnDto(
    val data: List<NumberingRuleListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
