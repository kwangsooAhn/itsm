/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIAttributeValueDto
import co.brainz.cmdb.dto.CIGroupListDto
import co.brainz.cmdb.dto.CISearchItem
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition

interface CIAttributeRepositoryCustom : AliceRepositoryCustom {
    fun findAttributeList(ciAttributeSearchCondition: CIAttributeSearchCondition): PagingReturnDto
    fun findAttribute(attributeId: String): CIAttributeListDto
    fun findAttributeDetail(attributeId: String): CIAttributeDto
    fun findDuplicationAttributeName(attributeName: String, attributeId: String): Long
    fun findAttributeValueList(ciId: String, classId: String): List<CIAttributeValueDto>
    fun findAttributeListWithoutGroupList(
        attributeId: String,
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): PagingReturnDto

    fun findAttributeListInGroupList(attributeIdList: MutableList<String>): List<CIAttributeValueDto>
    fun findGroupListData(attributeId: String, ciId: String): List<CIGroupListDto>
    fun findAttributeList(attributeIds: Set<String>): List<CISearchItem>
}
