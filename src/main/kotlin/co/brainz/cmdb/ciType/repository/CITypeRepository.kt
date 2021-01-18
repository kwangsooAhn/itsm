/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CmdbTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CITypeRepository : JpaRepository<CmdbTypeEntity, String>, CITypeRepositoryCustom {
}
