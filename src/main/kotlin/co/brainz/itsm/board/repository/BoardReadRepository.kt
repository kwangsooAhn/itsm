package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardReadEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardReadRepository: JpaRepository<PortalBoardReadEntity, String> {
    fun findByBoardId(boardId: String): List<PortalBoardReadEntity>
    fun countByBoardId(boardId: String): Long
}