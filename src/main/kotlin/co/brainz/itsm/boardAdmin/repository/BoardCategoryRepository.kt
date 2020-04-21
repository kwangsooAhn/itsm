package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardCategoryRepository: JpaRepository<PortalBoardCategoryEntity, String> {
    fun findByBoardAdminIdOrderByBoardCategorySortAsc(boardAdminId: String): List<PortalBoardCategoryEntity>
    fun deleteByBoardAdminId(boardAdminId: String)
}