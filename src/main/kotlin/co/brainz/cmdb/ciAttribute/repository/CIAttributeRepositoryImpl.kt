/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.ciAttribute.entity.CmdbAttributeEntity
import co.brainz.cmdb.ciAttribute.entity.QCmdbAttributeEntity
import co.brainz.cmdb.ciClass.entity.QCmdbClassAttributeMapEntity
import co.brainz.cmdb.provider.dto.CmdbAttributeDto
import co.brainz.cmdb.provider.dto.CmdbAttributeListDto
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIAttributeRepositoryImpl : QuerydslRepositorySupport(CmdbAttributeEntity::class.java),
    CIAttributeRepositoryCustom {
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
                super.like(attribute.attributeName, search)
                    ?.or(super.like(attribute.attributeType, search))
                    ?.or(super.like(attribute.attributeText, search))
                    ?.or(super.like(attribute.attributeDesc, search))
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

    override fun findAttribute(attributeId: String): CmdbAttributeDto {
        val attribute = QCmdbAttributeEntity.cmdbAttributeEntity
        val classAttributeMap = QCmdbClassAttributeMapEntity.cmdbClassAttributeMapEntity
        return from(attribute)
            .select(
                Projections.constructor(
                    CmdbAttributeDto::class.java,
                    attribute.attributeId,
                    attribute.attributeName,
                    attribute.attributeDesc,
                    attribute.attributeText,
                    attribute.attributeType,
                    attribute.attributeValue,
                    attribute.createUser.userKey,
                    attribute.createDt,
                    attribute.updateUser.userKey,
                    attribute.updateDt,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(!classAttributeMap.cmdbAttribute.attributeId.count().gt(0))
                            .from(classAttributeMap)
                            .where(classAttributeMap.cmdbAttribute.attributeId.eq(attribute.attributeId)), "enabled"
                    ),
                    Expressions.asString("")
                )
            )
            .where(attribute.attributeId.eq(attributeId))
            .fetchOne()
    }
}
