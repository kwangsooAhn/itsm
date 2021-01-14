/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIClassRepositoryImpl : QuerydslRepositorySupport(CmdbClassEntity::class.java), CIClassRepositoryCustom {
}
