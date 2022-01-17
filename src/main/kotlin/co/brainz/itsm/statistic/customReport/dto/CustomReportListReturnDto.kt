/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class CustomReportListReturnDto(
    val data: List<CustomReportListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
