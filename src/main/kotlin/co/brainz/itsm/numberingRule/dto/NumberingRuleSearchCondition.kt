/**
 * 문서번호 검색 조건용 데이터 클래스
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.itsm.numberingRule.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

/**
 * @param searchValue : 사용자가 입력한 검색어
 * @param pageNum : 검색결과중에서 요청받은 페이지
 * @param contentNumPerPage : 페이지당 출력되는 건수
 */
data class NumberingRuleSearchCondition(
    val searchValue: String? = null,
    val pageNum: Long = 1L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable
