/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.dto.BoardSearchCondition
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface BoardAdminRepositoryCustom : AliceRepositoryCustom {

    fun findByBoardAdminList(boardSearchCondition: BoardSearchCondition): Page<BoardListDto>

    fun findPortalBoardAdmin(): List<BoardListDto>
}
