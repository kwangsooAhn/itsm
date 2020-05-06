package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface BoardAdminRepository : JpaRepository<PortalBoardAdminEntity, String> {
    @Query(
        "SELECT pbm FROM PortalBoardAdminEntity pbm WHERE (LOWER(pbm.boardAdminTitle) LIKE LOWER(CONCAT('%', :search, '%')) " +
                " OR LOWER(pbm.createUser.userName) LIKE LOWER(CONCAT('%', :search,'%'))) " +
                " ORDER BY pbm.boardAdminId DESC"
    )
    fun findByBoardAdminList(search: String): List<PortalBoardAdminEntity>

    fun findAllByBoardUseYnTrueOrderByBoardAdminSortAsc(): MutableList<PortalBoardAdminEntity>
}
