/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.repository

import co.brainz.cmdb.attribute.entity.CmdbAttributeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AttributeRepository : JpaRepository<CmdbAttributeEntity, String>, AttributeRepositoryCustom {
}
