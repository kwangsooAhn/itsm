/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import java.io.Serializable

data class BoardArticleListReturnDto(
    val data: List<BoardArticleListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
