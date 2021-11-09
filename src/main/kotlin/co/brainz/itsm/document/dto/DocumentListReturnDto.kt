/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.document.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class DocumentListReturnDto(
    val data: List<DocumentDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
