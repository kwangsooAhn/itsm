/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.repository

import co.brainz.cmdb.type.entity.CmdbTypeEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface TypeRepositoryCustom : AliceRepositoryCustom {

    fun findByTypeList(search: String): QueryResults<CmdbTypeEntity>
}
