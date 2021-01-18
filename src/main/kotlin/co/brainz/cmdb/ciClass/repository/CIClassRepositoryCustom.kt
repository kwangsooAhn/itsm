/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.cmdb.provider.dto.CmdbClassToAttributeDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIClassRepositoryCustom : AliceRepositoryCustom {
    fun findClassList(search: String, offset: Long?): List<CmdbClassListDto>

    fun findClassToAttributeList(classList: MutableList<String>): List<CmdbClassToAttributeDto>?
}
