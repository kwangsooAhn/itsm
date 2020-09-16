/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardCategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BoardCategoryRepository : JpaRepository<PortalBoardCategoryEntity, String>, BoardCategoryRepositoryCustom
