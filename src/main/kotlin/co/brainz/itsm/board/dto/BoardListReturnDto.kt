/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.dto

import co.brainz.framework.util.AlicePagingData
import java.io.Serializable

data class BoardListReturnDto(
    val data: List<BoardListDto> = emptyList(),
    val paging: AlicePagingData
) : Serializable
