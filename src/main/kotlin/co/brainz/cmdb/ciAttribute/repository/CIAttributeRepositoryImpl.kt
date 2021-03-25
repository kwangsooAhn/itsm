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
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIAttributeValueDto
import co.brainz.cmdb.dto.SearchDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIAttributeRepositoryImpl : QuerydslRepositorySupport(CIAttributeEntity::class.java),
    CIAttributeRepositoryCustom {

    /**
     * Attribute 목록 조회.
     */
    override fun findAttributeList(searchDto: SearchDto): QueryResults<CIAttributeListDto> {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeListDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    ciAttribute.attributeDesc
                )
            )
            .where(
                super.like(ciAttribute.attributeName, searchDto.search)
                    ?.or(super.like(ciAttribute.attributeType, searchDto.search))
                    ?.or(super.like(ciAttribute.attributeText, searchDto.search))
                    ?.or(super.like(ciAttribute.attributeDesc, searchDto.search))
            ).orderBy(ciAttribute.attributeName.asc())
        if (searchDto.limit != null) {
            query.limit(searchDto.limit)
        }
        if (searchDto.offset != null) {
            query.offset(searchDto.offset)
        }
        return query.fetchResults()
    }

    /**
     * Attribute 목록 단일 조회.
     */
    override fun findAttribute(attributeId: String): CIAttributeListDto {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        return from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeListDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    ciAttribute.attributeDesc
                )
            )
            .where(ciAttribute.attributeId.eq(attributeId))
            .fetchOne()
    }

    /**
     * Attribute 상세 조회.
     */
    override fun findAttributeDetail(attributeId: String): CIAttributeDto {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val classAttributeMap = QCIClassAttributeMapEntity.cIClassAttributeMapEntity
        return from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeDesc,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    ciAttribute.attributeValue,
                    ciAttribute.createUser.userKey,
                    ciAttribute.createDt,
                    ciAttribute.updateUser.userKey,
                    ciAttribute.updateDt,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(!classAttributeMap.ciAttribute.attributeId.count().gt(0))
                            .from(classAttributeMap)
                            .where(classAttributeMap.ciAttribute.attributeId.eq(ciAttribute.attributeId)), "enabled"
                    )
                )
            )
            .where(ciAttribute.attributeId.eq(attributeId))
            .fetchOne()
    }

    /**
     * Attribute 명 중복 체크.
     */
    override fun findDuplicationAttributeName(attributeName: String, attributeId: String): Long {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciAttribute)
            .where(ciAttribute.attributeName.eq(attributeName))
        if (attributeId.isNotEmpty()) {
            query.where(!ciAttribute.attributeId.eq(attributeId))
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
