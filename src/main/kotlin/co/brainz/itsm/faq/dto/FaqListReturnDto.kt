/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.dto

import java.io.Serializable

data class FaqListReturnDto(
    val data: List<FaqListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
