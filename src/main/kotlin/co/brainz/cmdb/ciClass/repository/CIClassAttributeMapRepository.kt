/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CIClassAttributeMapEntity
import co.brainz.cmdb.ciClass.entity.CIClassAttributeMapPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIClassAttributeMapRepository : JpaRepository<CIClassAttributeMapEntity, CIClassAttributeMapPk>
