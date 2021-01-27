/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.provider.dto.CIAttributeDto
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface CIAttributeRepositoryCustom : AliceRepositoryCustom {
    fun findAttributeList(search: String, offset: Long?): List<CIAttributeListDto>
    fun findAttribute(attributeId: String): CIAttributeDto
    fun findDuplicationAttributeName(attributeName: String, attributeId: String): Long
}
