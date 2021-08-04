/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.notice.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class NoticeListReturnDto(
    val data: List<NoticeListDto> = emptyList(),
    val pagingData: AlicePagingData
) : Serializable
