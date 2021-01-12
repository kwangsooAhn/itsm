/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.repository

import co.brainz.cmdb.`class`.entity.CmdbClassAttributeMapEntity
import co.brainz.cmdb.`class`.entity.CmdbClassAttributeMapPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CmdbClassAttributeMapRepository : JpaRepository<CmdbClassAttributeMapEntity, CmdbClassAttributeMapPk>
