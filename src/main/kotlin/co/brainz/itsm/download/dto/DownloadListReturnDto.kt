/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.download.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class DownloadListReturnDto(
    val data: List<DownloadListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
