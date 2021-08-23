/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class ReportListReturnDto(
    val data: List<ReportListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
