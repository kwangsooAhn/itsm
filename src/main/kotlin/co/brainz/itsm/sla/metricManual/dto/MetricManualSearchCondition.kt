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
 * @param searchValue : 사용자가 입력한 검색어
 * @param fromDt : 생성일자 시작일 조건
 * @param toDt : 생성일자 종료일 조건
 * @param pageNum : 검색결과중에서 요청받은 페이지
 * @param contentNumPerPage : 페이지당 출력되는 건수
 */
data class MetricManualSearchCondition(
    val searchValue: String? = "",
    val fromDt: LocalDate? = LocalDate.parse(
        LocalDate.of(LocalDate.now().year, 1, 1).toString(),
        DateTimeFormatter.ISO_DATE
    ),
    val toDt: LocalDate? = LocalDate.parse(
        LocalDate.of(LocalDate.now().year, 12, 1).toString(),
        DateTimeFormatter.ISO_DATE
    ),
    val pageNum: Long = 1L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable
