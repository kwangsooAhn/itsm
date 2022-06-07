/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

/**
 * @param year : 사용자가 선택한 년도
 * @param pageNum : 검색 결과 중에서 요청받은 페이지
 * @param contentNumPerPage : 페이지당 출력되는 건수
 */
data class MetricYearSearchCondition(
    val year: String,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
