/**
 * 공지사항 검색 조건용 데이터 클래스
 *
 *  - 인스턴스 생성시 화면에서 받은 날짜 스트링을 포맷에 맞추어 변경한 값을 가진다.
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.notice.dto

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
data class NoticeSearchCondition(
    val searchValue: String? = null,
    val fromDt: String? = null,
    val toDt: String? = null,
    val pageNum: Long = 1L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val formattedFromDt: LocalDateTime = LocalDateTime.parse(fromDt, DateTimeFormatter.ISO_DATE_TIME)
    val formattedToDt: LocalDateTime = LocalDateTime.parse(toDt, DateTimeFormatter.ISO_DATE_TIME)
}
