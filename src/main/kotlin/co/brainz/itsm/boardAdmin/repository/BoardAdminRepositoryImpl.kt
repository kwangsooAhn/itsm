/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.boardAdmin.dto.BoardAdminListDto
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.boardAdmin.entity.QPortalBoardAdminEntity
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
    ): List<BoardAdminListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        val board = QPortalBoardEntity("board")
        return from(boardAdmin)
            .select(
                Projections.constructor(
                    BoardAdminListDto::class.java,
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
                super.likeIgnoreCase(
                    boardAdmin.boardAdminTitle, search
                )?.or(super.likeIgnoreCase(boardAdmin.createUser.userName, search))
            ).orderBy(boardAdmin.createDt.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)
            .fetch()
    }

    override fun findPortalBoardAdmin(): List<BoardAdminListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        return from(boardAdmin)
            .select(
                Projections.constructor(
                    BoardAdminListDto::class.java,
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
