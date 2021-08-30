/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.board.dto.BoardArticleListDto
import co.brainz.itsm.board.dto.BoardArticleSearchCondition
import co.brainz.itsm.board.dto.BoardArticleViewDto
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.QPortalBoardAdminEntity
import co.brainz.itsm.board.entity.QPortalBoardCategoryEntity
import co.brainz.itsm.board.entity.QPortalBoardCommentEntity
import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.board.entity.QPortalBoardReadEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardRepositoryImpl : QuerydslRepositorySupport(PortalBoardEntity::class.java), BoardRepositoryCustom {
    override fun findByBoardList(boardArticleSearchCondition: BoardArticleSearchCondition): QueryResults<BoardArticleListDto> {
        val board = QPortalBoardEntity.portalBoardEntity
        val user = QAliceUserEntity.aliceUserEntity
        val category = QPortalBoardCategoryEntity("category")
        val boardRead = QPortalBoardReadEntity("read")
        val comment = QPortalBoardCommentEntity("comment")
        val boardAdmin = QPortalBoardAdminEntity("categoryYn")
        return from(board)
            .select(
                Projections.constructor(
                    BoardArticleListDto::class.java,
                    board.boardId,
                    board.boardAdmin.boardAdminId,
                    category.boardCategoryName,
                    board.boardSeq,
                    board.boardGroupId,
                    board.boardLevelId,
                    board.boardTitle,
                    boardAdmin.categoryYn,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(comment.boardCommentId.count()).from(comment)
                            .where(board.boardId.eq(comment.commentBoard.boardId)), "replyCount"
                    ),
                    boardRead.boardReadCount.coalesce(0).`as`("readCount"),
                    board.createDt,
                    board.createUser.userName
                )
            )
            .innerJoin(board.createUser, user)
            .leftJoin(category).on(board.boardCategoryId.eq(category.boardCategoryId))
            .leftJoin(boardRead).on(board.boardId.eq(boardRead.boardId))
            .leftJoin(boardAdmin).on((board.boardAdmin.boardAdminId.eq(boardAdmin.boardAdminId)))
            .where(
                board.boardAdmin.boardAdminId.eq(boardArticleSearchCondition.boardAdminId),
                super.likeIgnoreCase(
                    board.boardTitle, boardArticleSearchCondition.searchValue
                )?.or(super.likeIgnoreCase(category.boardCategoryName, boardArticleSearchCondition.searchValue))
                    ?.or(super.likeIgnoreCase(board.createUser.userName, boardArticleSearchCondition.searchValue)),
                board.createDt.goe(boardArticleSearchCondition.formattedFromDt),
                board.createDt.lt(boardArticleSearchCondition.formattedToDt)
            )
            .orderBy(board.boardGroupId.desc(), board.boardOrderSeq.asc())
            .limit(boardArticleSearchCondition.contentNumPerPage)
            .offset((boardArticleSearchCondition.pageNum - 1) * boardArticleSearchCondition.contentNumPerPage)
            .fetchResults()
    }

    override fun findByBoardId(boardId: String): BoardArticleViewDto {
        val board = QPortalBoardEntity.portalBoardEntity

        return from(board)
            .select(
                Projections.constructor(
                    BoardArticleViewDto::class.java,
                    board.boardId,
                    board.boardAdmin,
                    board.boardCategoryId,
                    Expressions.asString(""),
                    board.boardTitle,
                    board.boardContents,
                    board.createDt,
                    board.createUser
                )
            )
            .where(board.boardId.eq(boardId))
            .fetchOne()
    }
}
