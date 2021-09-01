/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

/**
 * @param searchValue : 사용자가 입력한 검색어
 * @param groupCodes : FAQ 그룹 코드
 * @param pageNum : 검색결과중에서 요청받은 페이지
 * @param contentNumPerPage : 페이지당 출력되는 건수
 */
data class FaqSearchCondition(
    val searchValue: String? = null,
    var groupCodes: MutableList<String>? = null,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
