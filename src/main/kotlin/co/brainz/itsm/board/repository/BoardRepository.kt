package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardEntity
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardRepository : JpaRepository<PortalBoardEntity, String> {

    @Query("SELECT b FROM PortalBoardEntity b " +
            " LEFT OUTER JOIN PortalBoardCategoryEntity bc on b.boardCategoryId = bc.boardCategoryId " +
            " WHERE b.boardAdmin.boardAdminId = :boardAdminId " +
            " AND (LOWER(b.boardTitle) LIKE LOWER(CONCAT('%', :search, '%')) " +
            " OR LOWER(bc.boardCategoryName) LIKE LOWER(CONCAT('%', :search, '%')) " +
            " OR LOWER(b.createUser.userName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            " AND b.createDt BETWEEN :fromDt AND :toDt ORDER BY b.boardGroupNo DESC, b.boardLevelNo ASC, b.boardOrderSeq ASC")
    fun findByBoardList(boardAdminId: String, search: String, fromDt: LocalDateTime, toDt: LocalDateTime): List<PortalBoardEntity>

    @Query("SELECT COUNT(b.boardSeq) FROM PortalBoardEntity b WHERE b.boardAdmin.boardAdminId = :boardAdminId")
    fun countByBoardAdminId(boardAdminId: String): Long

    @Query("SELECT MAX(b.boardSeq) as boardSeq FROM PortalBoardEntity b WHERE b.boardAdmin.boardAdminId = :boardAdminId")
    fun findMaxBoardSeq(boardAdminId: String): Long

    @Modifying
    @Query("UPDATE PortalBoardEntity b SET b.boardOrderSeq =  b.boardOrderSeq + 1 " +
            "WHERE b.boardAdmin.boardAdminId = :boardAdminId AND b.boardGroupNo = :boardGroupNo AND b.boardOrderSeq >= :boardOrderSeq AND b.boardSeq <> :boardSeq")
    fun updateBoardOrderSeq(boardAdminId: String, boardGroupNo: Long, boardOrderSeq: Long, boardSeq: Long)
}
