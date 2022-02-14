/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

data class ChartSearchCondition(
    val searchGroupName: String? = null,
    val searchValue: String? = null,
    val pageNum: Long = 0L,
    val orderColName: String? = null,
    val orderDir: String? = null,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
