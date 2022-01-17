/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class CustomTemplateListReturnDto(
    val data: List<CustomTemplateListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
