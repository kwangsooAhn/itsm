/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable
import java.time.LocalDateTime
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
    val fromDt: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.of(LocalDateTime.now().year, 1, 1, 0, 0, 0).toString(),
        DateTimeFormatter.ISO_DATE_TIME
    ),
    val toDt: LocalDateTime? = LocalDateTime.parse(
        LocalDateTime.of(LocalDateTime.now().year, 12, 1, 0, 0, 0).toString(),
        DateTimeFormatter.ISO_DATE_TIME
    ),
    val pageNum: Long = 1L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable
