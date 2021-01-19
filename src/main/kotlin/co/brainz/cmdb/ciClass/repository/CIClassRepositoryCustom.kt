/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciClass.entity.CmdbClassEntity
import co.brainz.cmdb.provider.dto.CmdbClassToAttributeDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface CIClassRepositoryCustom : AliceRepositoryCustom {
    fun findClassList(search: String): QueryResults<CmdbClassEntity>

    fun findClassToAttributeList(classList: MutableList<String>): List<CmdbClassToAttributeDto>?
}
