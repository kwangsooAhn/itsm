/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIAttributeValueDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.querydsl.AliceRepositoryCustom
import com.querydsl.core.QueryResults

interface CIAttributeRepositoryCustom : AliceRepositoryCustom {
    fun findAttributeList(searchDto: SearchDto): QueryResults<CIAttributeListDto>
    fun findAttribute(attributeId: String): CIAttributeListDto
    fun findAttributeDetail(attributeId: String): CIAttributeDto
    fun findDuplicationAttributeName(attributeName: String, attributeId: String): Long
    fun findAttributeValueList(ciId: String, classId: String): List<CIAttributeValueDto>
}
