/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.report.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class ReportTemplateListReturnDto(
    val data: List<ReportTemplateListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
