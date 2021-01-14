/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CmdbTypeEntity
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CICITypeRepository : JpaRepository<CmdbTypeEntity, String>, CITypeRepositoryCustom {

    fun findTypeEntityByTypeId(typeId: String): Optional<CmdbTypeEntity>
}
