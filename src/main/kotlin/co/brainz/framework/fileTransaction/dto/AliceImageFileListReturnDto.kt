/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.fileTransaction.dto

import java.io.Serializable

data class AliceImageFileListReturnDto(
    val data: List<AliceImageFileDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
