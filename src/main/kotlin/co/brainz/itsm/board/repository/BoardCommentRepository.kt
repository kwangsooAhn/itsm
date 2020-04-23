package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardCommentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardCommentRepository: JpaRepository<PortalBoardCommentEntity, String> {
    fun findByBoardId(boardId: String): List<PortalBoardCommentEntity>
    fun findByBoardIdOrderByCreateDtDesc(boardId: String): List<PortalBoardCommentEntity>
    fun countByBoardId(boardId: String): Long
    fun deleteByBoardId(boardId: String)
}