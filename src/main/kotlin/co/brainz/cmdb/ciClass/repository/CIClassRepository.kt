/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIClassRepository : JpaRepository<CmdbClassEntity, String>, CIClassRepositoryCustom
