/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.dto.BoardSearchCondition
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.board.entity.QPortalBoardAdminEntity
import co.brainz.itsm.board.entity.QPortalBoardEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardAdminRepositoryImpl : QuerydslRepositorySupport(PortalBoardAdminEntity::class.java),
    BoardAdminRepositoryCustom {

    override fun findByBoardAdminList(boardSearchCondition: BoardSearchCondition): Page<BoardListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        val user = QAliceUserEntity.aliceUserEntity
        val board = QPortalBoardEntity("board")
        val pageable = Pageable.unpaged()
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
                    boardAdmin.createDt,
                    boardAdmin.createUser.userName
                )
            )
            .innerJoin(boardAdmin.createUser, user)
            .where(
                super.likeIgnoreCase(
                    boardAdmin.boardAdminTitle, boardSearchCondition.searchValue
                )?.or(super.likeIgnoreCase(boardAdmin.createUser.userName, boardSearchCondition.searchValue))
            ).orderBy(boardAdmin.createDt.desc())
        val totalCount = query.fetch().size
        if (boardSearchCondition.isPaging) {
            query.limit(boardSearchCondition.contentNumPerPage)
            query.offset((boardSearchCondition.pageNum - 1) * boardSearchCondition.contentNumPerPage)
        }

        return PageImpl<BoardListDto>(query.fetch(), pageable, totalCount.toLong())
    }

    override fun findPortalBoardAdmin(): List<BoardListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        val user = QAliceUserEntity.aliceUserEntity
        return from(boardAdmin)
            .select(
                Projections.constructor(
                    BoardListDto::class.java,
                    boardAdmin.boardAdminId,
                    boardAdmin.boardAdminTitle,
                    boardAdmin.categoryYn,
                    Expressions.numberPath(Long::class.java, "0"),
                    boardAdmin.createDt,
                    boardAdmin.createUser.userName
                )
            )
            .innerJoin(boardAdmin.createUser, user)
            .where(boardAdmin.boardUseYn.eq(true))
            .orderBy(boardAdmin.boardAdminSort.asc())
            .fetch()
    }
}
