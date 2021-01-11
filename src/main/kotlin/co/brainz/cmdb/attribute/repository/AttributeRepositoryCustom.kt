/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.repository

import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface AttributeRepositoryCustom : AliceRepositoryCustom {
    fun findAttributeList(search: String, offset: Long?): List<CmdbAttributeListDto>
}
