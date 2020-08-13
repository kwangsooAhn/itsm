/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardDto
import java.time.LocalDateTime

interface BoardRepositoryCustom : AliceRepositoryCustom {
    fun findByBoardList(
        boardAdminId: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): MutableList<BoardDto>

    fun findByBoardCount(
        boardAdminId: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): Long
}
