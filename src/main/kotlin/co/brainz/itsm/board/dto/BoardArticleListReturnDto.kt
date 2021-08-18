/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class BoardArticleListReturnDto(
    val data: List<BoardArticleListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
