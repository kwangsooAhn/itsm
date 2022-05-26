/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * @param metricId : 사용자가 선택한 지표 ID
 * @param fromDt : 생성일자 시작일 조건
 * @param toDt : 생성일자 종료일 조건
 * @param pageNum : 검색결과중에서 요청받은 페이지
 * @param contentNumPerPage : 페이지당 출력되는 건수
 */
data class MetricManualSearchCondition(
    val metricId: String? = "",
    val fromDt: String? = null,
    val toDt: String? = null,
    val pageNum: Long = 1L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val formattedFromDt: LocalDate? = LocalDate.parse(
        fromDt, DateTimeFormatter.ISO_DATE_TIME)
    val formattedToDt: LocalDate? = LocalDate.parse(
        toDt, DateTimeFormatter.ISO_DATE_TIME)
}
