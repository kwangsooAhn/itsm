/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.ci.entity.QCIDataEntity
import co.brainz.cmdb.ciAttribute.entity.CIAttributeEntity
import co.brainz.cmdb.ciAttribute.entity.QCIAttributeEntity
import co.brainz.cmdb.ciClass.entity.QCIClassAttributeMapEntity
import co.brainz.cmdb.provider.dto.CIAttributeDto
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.cmdb.provider.dto.CIAttributeValueDto
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIAttributeRepositoryImpl : QuerydslRepositorySupport(CIAttributeEntity::class.java),
    CIAttributeRepositoryCustom {

    /**
     * Attribute 목록 조회.
     */
    override fun findAttributeList(search: String, offset: Long?): List<CIAttributeListDto> {
        val attribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(attribute)
            .select(
                Projections.constructor(
                    CIAttributeListDto::class.java,
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
        val attributeList = mutableListOf<CIAttributeListDto>()
        for (data in result.results) {
            data.totalCount = result.total
            attributeList.add(data)
        }
        return attributeList.toList()
    }

    /**
     * Attribute 조회.
     */
    override fun findAttribute(attributeId: String): CIAttributeDto {
        val attribute = QCIAttributeEntity.cIAttributeEntity
        val classAttributeMap = QCIClassAttributeMapEntity.cIClassAttributeMapEntity
        return from(attribute)
            .select(
                Projections.constructor(
                    CIAttributeDto::class.java,
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
                        JPAExpressions.select(!classAttributeMap.ciAttribute.attributeId.count().gt(0))
                            .from(classAttributeMap)
                            .where(classAttributeMap.ciAttribute.attributeId.eq(attribute.attributeId)), "enabled"
                    )
                )
            )
            .where(attribute.attributeId.eq(attributeId))
            .fetchOne()
    }

    /**
     * Attribute 명 중복 체크.
     */
    override fun findDuplicationAttributeName(attributeName: String, attributeId: String): Long {
        val attribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(attribute)
            .where(attribute.attributeName.eq(attributeName))
        if (attributeId.isNotEmpty()) {
            query.where(!attribute.attributeId.eq(attributeId))
        }
        return query.fetchCount()
    }

    /**
     * Attribute, Value 리스트 조회
     */
    override fun findAttributeValueList(ciId: String, classId: String): List<CIAttributeValueDto> {
        val ciAttributeEntity = QCIAttributeEntity.cIAttributeEntity
        val ciDataEntity = QCIDataEntity.cIDataEntity
        val ciClassAttributeMapEntity = QCIClassAttributeMapEntity.cIClassAttributeMapEntity

        val query = from(ciAttributeEntity)
            .select(
                Projections.constructor(
                    CIAttributeValueDto::class.java,
                    ciAttributeEntity.attributeId,
                    ciAttributeEntity.attributeName,
                    ciAttributeEntity.attributeText,
                    ciAttributeEntity.attributeType,
                    ciClassAttributeMapEntity.attributeOrder,
                    ciAttributeEntity.attributeValue,
                    ciDataEntity.value
                )
            )
            .innerJoin(ciClassAttributeMapEntity)
            .on(
                (ciClassAttributeMapEntity.ciAttribute.attributeId.eq(ciAttributeEntity.attributeId))
                    .and(ciClassAttributeMapEntity.ciClass.classId.eq(classId))
            )
            .leftJoin(ciDataEntity)
            .on(
                (ciDataEntity.ciAttribute.attributeId.eq(ciAttributeEntity.attributeId))
                    .and(ciDataEntity.ci.ciId.eq(ciId))
            )
            .orderBy(ciClassAttributeMapEntity.attributeOrder.asc())

        val result = query.fetchResults()
        val ciAttributeDataList = mutableListOf<CIAttributeValueDto>()
        for (data in result.results) {
            ciAttributeDataList.add(data)
        }
        return ciAttributeDataList
    }
}
