/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardArticleListDto
import co.brainz.itsm.board.dto.BoardArticleSearchCondition
import co.brainz.itsm.board.dto.BoardArticleViewDto
import com.querydsl.core.QueryResults

interface BoardRepositoryCustom : AliceRepositoryCustom {
    fun findByBoardList(boardArticleSearchCondition: BoardArticleSearchCondition): QueryResults<BoardArticleListDto>

    fun findByBoardId(boardId: String): BoardArticleViewDto
}
