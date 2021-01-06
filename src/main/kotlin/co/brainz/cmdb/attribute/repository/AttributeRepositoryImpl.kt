/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.repository

import co.brainz.cmdb.attribute.entity.CmdbAttributeEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AttributeRepositoryImpl : QuerydslRepositorySupport(CmdbAttributeEntity::class.java), AttributeRepositoryCustom {

}
