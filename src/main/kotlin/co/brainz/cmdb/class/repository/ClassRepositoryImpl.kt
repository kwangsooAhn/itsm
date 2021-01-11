/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.`class`.repository

import co.brainz.cmdb.`class`.entity.CmdbClassEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ClassRepositoryImpl : QuerydslRepositorySupport(CmdbClassEntity::class.java), ClassRepositoryCustom {
}
