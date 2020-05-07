package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardCategoryRepository : JpaRepository<PortalBoardCategoryEntity, String> {
    @Query("SELECT c FROM PortalBoardCategoryEntity c WHERE c.boardAdmin.boardAdminId = :boardAdminId ORDER BY c.boardCategorySort ASC")
    fun findByBoardAdminOrderByBoardCategorySortAsc(boardAdminId: String): List<PortalBoardCategoryEntity>
}
