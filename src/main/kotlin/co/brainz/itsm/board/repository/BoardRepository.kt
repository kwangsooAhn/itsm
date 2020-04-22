package co.brainz.itsm.board.repository

import co.brainz.itsm.board.entity.PortalBoardEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface BoardRepository: JpaRepository<PortalBoardEntity, String> {

    @Query("SELECT b FROM PortalBoardEntity b " +
            "INNER JOIN PortalBoardAdminEntity ba ON b.boardAdminId = ba.boardAdminId" +
            " WHERE b.boardAdminId = :boardAdminId " +
            " AND (LOWER(b.boardTitle) LIKE LOWER(CONCAT('%', :search, '%')) " +
            " OR LOWER(b.createUser.userName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
            " AND b.createDt BETWEEN :fromDt AND :toDt ORDER BY b.boardNo DESC")
    fun findByBoardList(boardAdminId: String, search: String, fromDt: LocalDateTime, toDt: LocalDateTime): List<PortalBoardEntity>

    fun countByBoardAdminId(boardAdminId: String): Long

    @Query("SELECT MAX(b.boardNo) as boardNo FROM PortalBoardEntity b WHERE b.boardAdminId = :boardAdminId")
    fun findMaxBoardNo(boardAdminId: String) : Long
}