/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.repository

import co.brainz.cmdb.`class`.entity.CmdbClassEntity
import co.brainz.cmdb.attribute.repository.AttributeRepositoryCustom
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ClassRepositoryImpl : QuerydslRepositorySupport(CmdbClassEntity::class.java), AttributeRepositoryCustom {

}
