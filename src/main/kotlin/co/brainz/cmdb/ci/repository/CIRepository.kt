/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CmdbCiEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIRepository : JpaRepository<CmdbCiEntity, String>, CIRepositoryCustom {
}
