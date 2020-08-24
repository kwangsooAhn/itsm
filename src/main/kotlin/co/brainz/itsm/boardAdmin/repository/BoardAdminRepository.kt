/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardAdminRepository : JpaRepository<PortalBoardAdminEntity, String>, BoardAdminRepositoryCustom {
    fun findAllByBoardUseYnTrueOrderByBoardAdminSortAsc(): MutableList<PortalBoardAdminEntity>
}
