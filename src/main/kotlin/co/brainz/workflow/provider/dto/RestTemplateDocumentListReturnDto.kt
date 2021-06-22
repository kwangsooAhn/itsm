/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import java.io.Serializable

data class RestTemplateDocumentListReturnDto(
    val data: List<RestTemplateDocumentListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable