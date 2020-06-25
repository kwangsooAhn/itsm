package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.repository.querydsl.BoardRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : JpaRepository<PortalBoardEntity, String>,
    BoardRepositoryCustom {

    @Query("SELECT COUNT(b.boardSeq) FROM PortalBoardEntity b WHERE b.boardAdmin.boardAdminId = :boardAdminId")
    fun countByBoardAdminId(boardAdminId: String): Long

    @Query(
        "SELECT MAX(b.boardSeq) as boardSeq " +
                " FROM PortalBoardEntity b WHERE b.boardAdmin.boardAdminId = :boardAdminId"
    )
    fun findMaxBoardSeq(boardAdminId: String): Long

    @Modifying
    @Query(
        "UPDATE PortalBoardEntity b SET b.boardOrderSeq =  b.boardOrderSeq + 1 " +
                " WHERE b.boardAdmin.boardAdminId = :boardAdminId " +
                " AND b.boardGroupId = :boardGroupId AND b.boardOrderSeq >= :boardOrderSeq AND b.boardSeq <> :boardSeq"
    )
    fun updateBoardOrderSeq(boardAdminId: String, boardGroupId: Long, boardOrderSeq: Long, boardSeq: Long)
}
