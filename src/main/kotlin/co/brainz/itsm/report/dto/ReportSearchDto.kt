/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import co.brainz.itsm.constants.ItsmConstants
import java.io.Serializable

data class ReportSearchDto(
    val offset: Long? = 0,
    val search: String? = "",
    val isScroll: Boolean = false,
    val limit: Long? = ItsmConstants.SEARCH_DATA_COUNT
) : Serializable

