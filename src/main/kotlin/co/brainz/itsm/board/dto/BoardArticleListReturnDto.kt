package co.brainz.itsm.board.dto

import java.io.Serializable

data class BoardArticleListReturnDto(
    val data: List<BoardArticleListDto> = emptyList(),
    val totalCount: Long = 0
) : Serializable
