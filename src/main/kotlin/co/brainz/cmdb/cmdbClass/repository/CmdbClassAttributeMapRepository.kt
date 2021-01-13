/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.cmdbClass.repository

import co.brainz.cmdb.cmdbClass.entity.CmdbClassAttributeMapEntity
import co.brainz.cmdb.cmdbClass.entity.CmdbClassAttributeMapPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CmdbClassAttributeMapRepository : JpaRepository<CmdbClassAttributeMapEntity, CmdbClassAttributeMapPk>
