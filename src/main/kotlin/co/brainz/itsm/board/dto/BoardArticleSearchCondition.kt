/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import co.brainz.framework.constants.PagingConstants
import java.io.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BoardArticleSearchCondition(
    val boardAdminId: String,
    val searchValue: String? = null,
    val fromDt: String? = null,
    val toDt: String? = null,
    val pageNum: Long = 0L,
    val contentNumPerPage: Long = PagingConstants.COUNT_PER_PAGE
) : Serializable {
    val formattedFromDt: LocalDateTime? = fromDt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
    val formattedToDt: LocalDateTime? = toDt?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_DATE_TIME) }
    val isPaging = pageNum > 0
}
