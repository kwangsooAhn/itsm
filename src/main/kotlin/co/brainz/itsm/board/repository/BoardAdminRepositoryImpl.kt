/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.board.entity.QPortalBoardAdminEntity
import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardAdminRepositoryImpl : QuerydslRepositorySupport(PortalBoardAdminEntity::class.java),
    BoardAdminRepositoryCustom {

    override fun findByBoardAdminList(
        search: String,
        offset: Long
    ): List<BoardListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        val board = QPortalBoardEntity("board")
        val query = from(boardAdmin)
            .select(
                Projections.constructor(
                    BoardListDto::class.java,
                    boardAdmin.boardAdminId,
                    boardAdmin.boardAdminTitle,
                    boardAdmin.categoryYn,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(board.boardId.count()).from(board)
                            .where(board.boardAdmin.boardAdminId.eq(boardAdmin.boardAdminId)), "boardBoardCount"
                    ),
                    ExpressionUtils.`as`(
                        JPAExpressions.select(board.boardId.count()).from(board), "totalCount"
                    ),
                    boardAdmin.createDt,
                    boardAdmin.createUser.userName
                )
            ).where(
                super.like(
                    boardAdmin.boardAdminTitle, search
                )?.or(super.like(boardAdmin.createUser.userName, search))
            ).orderBy(boardAdmin.createDt.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)
            .fetchResults()

        val boardList = mutableListOf<BoardListDto>()
        for (data in query.results) {
            val boardListDto = BoardListDto(
                boardAdminId = data.boardAdminId,
                boardAdminTitle = data.boardAdminTitle,
                categoryYn = data.categoryYn,
                boardBoardCount = data.boardBoardCount,
                totalCount = query.total,
                createDt = data.createDt,
                createUserName = data.createUserName
            )
            boardList.add(boardListDto)
        }
        return boardList.toList()
    }

    override fun findPortalBoardAdmin(): List<BoardListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        return from(boardAdmin)
            .select(
                Projections.constructor(
                    BoardListDto::class.java,
                    boardAdmin.boardAdminId,
                    boardAdmin.boardAdminTitle,
                    boardAdmin.categoryYn,
                    Expressions.numberPath(Long::class.java, "0"),
                    Expressions.numberPath(Long::class.java, "0"),
                    boardAdmin.createDt,
                    boardAdmin.createUser.userName
                )
            )
            .where(boardAdmin.boardUseYn.eq(true))
            .orderBy(boardAdmin.boardAdminSort.asc())
            .fetch()
    }
}
