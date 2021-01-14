/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.repository

import co.brainz.cmdb.type.entity.CmdbTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TypeRepository : JpaRepository<CmdbTypeEntity, String>, TypeRepositoryCustom {

    fun findTypeEntityByTypeId(typeId: String): Optional<CmdbTypeEntity>
}
