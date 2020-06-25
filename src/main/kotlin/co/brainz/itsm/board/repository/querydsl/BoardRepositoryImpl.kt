package co.brainz.itsm.board.repository.querydsl

import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.QPortalBoardCommentEntity
import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.board.entity.QPortalBoardReadEntity
import co.brainz.itsm.boardAdmin.entity.QPortalBoardCategoryEntity
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
        toDt: LocalDateTime
    ): MutableList<BoardListDto> {
        val board = QPortalBoardEntity.portalBoardEntity
        val category = QPortalBoardCategoryEntity("category")
        val boardRead = QPortalBoardReadEntity("read")
        val comment = QPortalBoardCommentEntity("comment")
        return from(board)
            .select(
                Projections.constructor(
                    BoardListDto::class.java,
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
                    ExpressionUtils.`as`(
                        JPAExpressions.select(boardRead.boardId.count()).from(boardRead)
                            .where(board.boardId.eq(boardRead.boardId)), "readCount"
                    ),
                    board.createDt,
                    board.createUser
                )
            )
            .leftJoin(category).on(board.boardCategoryId.eq(category.boardCategoryId))
            .where(
                board.boardAdmin.boardAdminId.eq(boardAdminId),
                super.likeIgnoreCase(
                    board.boardTitle, search
                )?.or(super.likeIgnoreCase(category.boardCategoryName, search))
                    ?.or(super.likeIgnoreCase(board.createUser.userName, search)),
                board.createDt.goe(fromDt),
                board.createDt.lt(toDt)
            )
            .fetch()
    }
}