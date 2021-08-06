/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import java.io.Serializable

data class ReportTemplateListReturnDto(
    val data: List<ReportTemplateListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
