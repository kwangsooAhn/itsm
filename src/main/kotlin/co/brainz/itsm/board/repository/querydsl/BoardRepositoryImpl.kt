/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository.querydsl

import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.QPortalBoardCommentEntity
import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.board.entity.QPortalBoardReadEntity
import co.brainz.itsm.boardAdmin.entity.QPortalBoardCategoryEntity
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardRepositoryImpl : QuerydslRepositorySupport(PortalBoardEntity::class.java), BoardRepositoryCustom {
    override fun findByBoardList(
        boardAdminId: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): MutableList<BoardDto> {
        val board = QPortalBoardEntity.portalBoardEntity
        val category = QPortalBoardCategoryEntity("category")
        val boardRead = QPortalBoardReadEntity("read")
        val comment = QPortalBoardCommentEntity("comment")
        return from(board)
            .select(
                Projections.constructor(
                    BoardDto::class.java,
                    board.boardId,
                    board.boardAdmin.boardAdminId,
                    category.boardCategoryName,
                    board.boardSeq,
                    board.boardGroupId,
                    board.boardLevelId,
                    board.boardTitle,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(comment.boardCommentId.count()).from(comment)
                            .where(board.boardId.eq(comment.commentBoard.boardId)), "replyCount"
                    ),
                    boardRead.boardReadCount.coalesce(0).`as`("readCount"),
                    board.createDt,
                    board.createUser
                )
            )
            .leftJoin(category).on(board.boardCategoryId.eq(category.boardCategoryId))
            .leftJoin(boardRead).on(board.boardId.eq(boardRead.boardId))
            .where(
                board.boardAdmin.boardAdminId.eq(boardAdminId),
                super.likeIgnoreCase(
                    board.boardTitle, search
                )?.or(super.likeIgnoreCase(category.boardCategoryName, search))
                    ?.or(super.likeIgnoreCase(board.createUser.userName, search)),
                board.createDt.goe(fromDt),
                board.createDt.lt(toDt)
            )
            .orderBy(board.boardGroupId.desc(), board.boardOrderSeq.asc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)
            .fetch()
    }

    override fun findByBoardCount(
        boardAdminId: String,
        search: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
    ): Long {
        val board = QPortalBoardEntity.portalBoardEntity
        val category = QPortalBoardCategoryEntity("category")
        val boardRead = QPortalBoardReadEntity("read")
        return from(board)
            .leftJoin(category).on(board.boardCategoryId.eq(category.boardCategoryId))
            .leftJoin(boardRead).on(board.boardId.eq(boardRead.boardId))
            .where(
                board.boardAdmin.boardAdminId.eq(boardAdminId),
                super.likeIgnoreCase(
                    board.boardTitle, search
                )?.or(super.likeIgnoreCase(category.boardCategoryName, search))
                    ?.or(super.likeIgnoreCase(board.createUser.userName, search)),
                board.createDt.goe(fromDt),
                board.createDt.lt(toDt)
            )
            .fetchCount()
    }
}
