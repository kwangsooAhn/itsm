package co.brainz.itsm.board.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardListDto
import java.time.LocalDateTime

interface BoardRepositoryCustom : AliceRepositoryCustom {
    fun findByBoardList(
        boardAdminId: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): MutableList<BoardListDto>
}