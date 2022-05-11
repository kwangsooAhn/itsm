/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.archive.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class ArchiveListReturnDto(
    val data: List<ArchiveListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
