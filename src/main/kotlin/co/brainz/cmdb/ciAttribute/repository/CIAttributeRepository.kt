/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.ciAttribute.entity.CmdbAttributeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIAttributeRepository : JpaRepository<CmdbAttributeEntity, String>, CIAttributeRepositoryCustom {

    fun findByAttributeId(attributeId: String): CmdbAttributeEntity?
}
