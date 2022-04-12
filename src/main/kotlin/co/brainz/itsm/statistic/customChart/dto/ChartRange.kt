/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.dto

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * 차트 데이터를 수집하는 대상 기간용 객체.
 * 시작일, 종료일 기준으로 크게 3가지 형태의 셋을 가지고 있다.
 *
 * fromDate, toDate
 * - 차트 설정에서는 캘린더를 통해서 시작일과 종료일을 날짜로 저장하고 있으며, 이 설정 데이터를 가져온다.
 *
 * fromDateTime, toDateTime
 * - 날짜만 있는 설정 데이터에 시간 개념을 추가한 필드. 이 데이터로 카테고리를 설정하거나 화면에서 형식을 결정한다.
 *
 * fromDateTimeUTC, toDateTimeUTC
 * - 차트 설정의 날짜 시간은 사용자가 차트용 타임존이 적용된 것으로 생각하는 데이터이다.
 * - 이 데이터를 실제 데이터 검색 시 비교하기 위해서 UTC 로 변환한 시간.
 */
data class ChartRange(
    val type: String,
    val fromDate: LocalDate? = null,
    val toDate: LocalDate? = null
) : Serializable {
    val fromDateTime: LocalDateTime? = fromDate?.atTime(0, 0, 0)
    val toDateTime: LocalDateTime? = toDate?.atTime(23, 59, 59)
    var fromDateTimeUTC: LocalDateTime? = null
    var toDateTimeUTC: LocalDateTime? = null
}
