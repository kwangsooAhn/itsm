/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.board.dto.BoardArticleSearchCondition
import co.brainz.itsm.board.dto.BoardArticleViewDto

interface BoardRepositoryCustom : AliceRepositoryCustom {
    fun findByBoardList(boardArticleSearchCondition: BoardArticleSearchCondition): PagingReturnDto

    fun findByBoardId(boardId: String): BoardArticleViewDto
}
