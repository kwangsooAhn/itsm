/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.dto

import co.brainz.itsm.constants.ItsmConstants
import java.io.Serializable

data class ReportSearchDto(
    var offset: Long = 0,
    var search: String? = "",
    var isScroll: Boolean = false,
    var limit: Long = ItsmConstants.SEARCH_DATA_COUNT
) : Serializable

