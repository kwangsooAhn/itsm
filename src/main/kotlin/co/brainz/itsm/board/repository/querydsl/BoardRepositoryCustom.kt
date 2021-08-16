/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository.querydsl

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardArticleListReturnDto
import co.brainz.itsm.board.dto.BoardArticleSearchCondition
import co.brainz.itsm.board.dto.BoardArticleViewDto
import java.time.LocalDateTime

interface BoardRepositoryCustom : AliceRepositoryCustom {
    fun findByBoardList(boardArticleSearchCondition: BoardArticleSearchCondition): BoardArticleListReturnDto

    fun findByBoardId(boardId: String): BoardArticleViewDto
}
