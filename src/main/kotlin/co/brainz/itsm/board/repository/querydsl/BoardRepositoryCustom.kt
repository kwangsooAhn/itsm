/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardListDto
import java.time.LocalDateTime

interface BoardRepositoryCustom : AliceRepositoryCustom {
    fun findByBoardList(
        boardAdminId: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): List<BoardListDto>
}
