package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository: JpaRepository<PortalBoardEntity, String> {
    fun findPortalBoardEntityByBoardAdminId(boardAdminId: String): List<PortalBoardEntity>

    fun countByBoardAdminId(boardAdminId: String): Long
}