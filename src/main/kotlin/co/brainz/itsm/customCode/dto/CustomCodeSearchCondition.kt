/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable

data class CustomCodeSearchCondition(
    val viewType: String? = null,
    val searchValue: String? = null,
    val searchType: String? = null,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val isPaging = pageNum > 0
}
