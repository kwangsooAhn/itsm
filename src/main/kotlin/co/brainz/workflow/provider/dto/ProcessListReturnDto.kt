/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.provider.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class ProcessListReturnDto(
    val data: List<RestTemplateProcessViewDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
