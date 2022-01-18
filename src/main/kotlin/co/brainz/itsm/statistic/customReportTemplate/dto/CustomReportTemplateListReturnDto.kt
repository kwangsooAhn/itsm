/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReportTemplate.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class CustomReportTemplateListReturnDto(
    val data: List<CustomReportTemplateListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
