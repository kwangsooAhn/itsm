/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CmdbTypeEntity
import co.brainz.cmdb.provider.dto.CmdbTypeDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CITypeRepository : JpaRepository<CmdbTypeEntity, String>, CITypeRepositoryCustom {

    @Query(
        "SELECT NEW co.brainz.cmdb.provider.dto.CmdbTypeDto(t.typeId, t.typeName, t.typeDesc, t.typeLevel, " +
                "t.defaultClassId, t.pType.typeId, t.pType.typeId, t.typeIcon, t.createUser.userKey, t.createDt, t.updateUser.userKey, t.updateDt) FROM CmdbTypeEntity t " +
                "WHERE t.typeId = :typeId "
    )
    fun findByTypeId(typeId: String): CmdbTypeDto
}
