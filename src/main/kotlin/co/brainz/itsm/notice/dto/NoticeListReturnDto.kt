/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.dto

import java.io.Serializable

data class NoticeListReturnDto(
    val data: List<NoticeListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
