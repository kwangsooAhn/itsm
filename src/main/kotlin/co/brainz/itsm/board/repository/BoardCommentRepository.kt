package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardCommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardCommentRepository : JpaRepository<PortalBoardCommentEntity, String> {
    @Query("SELECT c FROM PortalBoardCommentEntity c WHERE c.commentBoard.boardId = :boardId")
    fun findByBoardId(boardId: String): List<PortalBoardCommentEntity>

    @Query("SELECT c FROM PortalBoardCommentEntity c WHERE c.commentBoard.boardId = :boardId ORDER BY c.createDt DESC")
    fun findByBoardIdOrderByCreateDtDesc(boardId: String): List<PortalBoardCommentEntity>

    @Query("SELECT COUNT(C) FROM PortalBoardCommentEntity c WHERE c.commentBoard.boardId = :boardId")
    fun countByBoardId(boardId: String): Long

    @Query("DELETE FROM PortalBoardCommentEntity c WHERE c.commentBoard.boardId = :boardId")
    fun deleteByBoardId(boardId: String)
}
