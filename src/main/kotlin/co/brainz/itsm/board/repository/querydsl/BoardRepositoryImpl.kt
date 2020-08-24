/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.repository.querydsl

import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardListDto
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
    ): List<BoardListDto> {
        val board = QPortalBoardEntity.portalBoardEntity
        val category = QPortalBoardCategoryEntity("category")
        val boardRead = QPortalBoardReadEntity("read")
        val comment = QPortalBoardCommentEntity("comment")
        val query = from(board)
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
            .fetchResults()

        val boardList = mutableListOf<BoardListDto>()
        for (data in query.results) {
            val boardListDto = BoardListDto(
                boardId = data.boardId,
                boardAdminId = data.boardAdminId,
                boardCategoryName = data.boardCategoryName,
                boardSeq = data.boardSeq,
                boardGroupId = data.boardGroupId,
                boardLevelId = data.boardLevelId,
                boardTitle = data.boardTitle,
                replyCount = data.replyCount,
                readCount = data.readCount,
                totalCount = query.total,
                createDt = data.createDt,
                createUserName = data.createUser?.userName
            )
            boardList.add(boardListDto)
        }
        return boardList.toList()
    }
}
