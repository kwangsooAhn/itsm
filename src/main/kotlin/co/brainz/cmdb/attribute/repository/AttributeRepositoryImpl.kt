/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.attribute.repository

import co.brainz.cmdb.attribute.entity.CmdbAttributeEntity
import co.brainz.cmdb.attribute.entity.QCmdbAttributeEntity
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AttributeRepositoryImpl : QuerydslRepositorySupport(CmdbAttributeEntity::class.java), AttributeRepositoryCustom {
    override fun findAttributeList(search: String, offset: Long?): List<CmdbAttributeListDto> {
        val attribute = QCmdbAttributeEntity.cmdbAttributeEntity
        val query = from(attribute)
            .select(
                Projections.constructor(
                    CmdbAttributeListDto::class.java,
                    attribute.attributeId,
                    attribute.attributeName,
                    attribute.attributeText,
                    attribute.attributeType,
                    attribute.attributeDesc,
                    Expressions.numberPath(Long::class.java, "0")
                )
            )
            .where(
                super.likeIgnoreCase(attribute.attributeName, search)
                    ?.or(super.likeIgnoreCase(attribute.attributeType, search))
                    ?.or(super.likeIgnoreCase(attribute.attributeText, search))
                    ?.or(super.likeIgnoreCase(attribute.attributeDesc, search))
            ).orderBy(attribute.attributeName.asc())
        if (offset != null) {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT)
                .offset(offset)
        }
        val result = query.fetchResults()
        val attributeList = mutableListOf<CmdbAttributeListDto>()
        for (data in result.results) {
            data.totalCount = result.total
            attributeList.add(data)
        }
        return attributeList.toList()
    }
}
