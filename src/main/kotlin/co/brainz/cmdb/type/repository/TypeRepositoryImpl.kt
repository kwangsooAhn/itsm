/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.repository

import co.brainz.cmdb.type.entity.CmdbTypeEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class TypeRepositoryImpl : QuerydslRepositorySupport(CmdbTypeEntity::class.java), TypeRepositoryCustom {
}
