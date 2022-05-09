/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.querydsl.dto

import java.io.Serializable

data class PagingReturnDto(
    val dataList: List<Any>,
    val totalCount: Long = 0L
): Serializable
