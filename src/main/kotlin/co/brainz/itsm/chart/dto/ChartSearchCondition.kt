/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.querydsl.QuerydslConstants
import java.io.Serializable

data class ChartSearchCondition(
    val searchGroupName: String? = null,
    val searchValue: String? = null,
    val pageNum: Long = 0L,
    val orderColName: String? = null,
    val orderDir: String? = QuerydslConstants.OrderSpecifier.ASC.code,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
