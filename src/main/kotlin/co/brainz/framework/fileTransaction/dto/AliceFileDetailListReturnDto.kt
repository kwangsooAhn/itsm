/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class AliceFileDetailListReturnDto(
    val data: List<AliceFileDetailDto> = emptyList(),
    val totalCount: Long = 0,
    val totalCountWithoutCondition: Long = 0
) : Serializable
