/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIAttributeRepositoryCustom : AliceRepositoryCustom {
    fun findAttributeList(search: String, offset: Long?): List<CmdbAttributeListDto>
    fun findAttribute(attributeId: String): CmdbAttributeDto
}
