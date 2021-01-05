/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.attribute.repository.AttributeRepositoryCustom
import co.brainz.cmdb.ci.entity.CmdbCiEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CiRepositoryImpl : QuerydslRepositorySupport(CmdbCiEntity::class.java), AttributeRepositoryCustom {
}
