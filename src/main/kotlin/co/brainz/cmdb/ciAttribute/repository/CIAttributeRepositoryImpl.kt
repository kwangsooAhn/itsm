/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciAttribute.repository

import co.brainz.cmdb.ci.entity.QCIDataEntity
import co.brainz.cmdb.ci.entity.QCIGroupListDataEntity
import co.brainz.cmdb.ciAttribute.entity.CIAttributeEntity
import co.brainz.cmdb.ciAttribute.entity.QCIAttributeEntity
import co.brainz.cmdb.ciClass.entity.QCIClassAttributeMapEntity
import co.brainz.cmdb.constants.RestTemplateConstants
import co.brainz.cmdb.dto.CIAttributeDto
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIAttributeValueDto
import co.brainz.cmdb.dto.CIGroupListDto
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import com.querydsl.core.QueryResults
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
    override fun findAttributeList(ciAttributeSearchCondition: CIAttributeSearchCondition): QueryResults<CIAttributeListDto> {
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
                super.like(ciAttribute.attributeName, ciAttributeSearchCondition.searchValue)
                    ?.or(super.like(ciAttribute.attributeType, ciAttributeSearchCondition.searchValue))
                    ?.or(super.like(ciAttribute.attributeText, ciAttributeSearchCondition.searchValue))
                    ?.or(super.like(ciAttribute.attributeDesc, ciAttributeSearchCondition.searchValue))
            ).orderBy(ciAttribute.attributeName.asc())

        if (ciAttributeSearchCondition.isPaging) {
            query.limit(ciAttributeSearchCondition.contentNumPerPage)
            query.offset((ciAttributeSearchCondition.pageNum - 1) * ciAttributeSearchCondition.contentNumPerPage)
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
                    ciAttribute.mappingId,
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
    override fun findAttributeValueList(ciId: String, classId: String): QueryResults<CIAttributeValueDto> {
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

        return query.fetchResults()
    }

    /**
     * Attribute 목록 조회 (Group List 제외).
     */
    override fun findAttributeListWithoutGroupList(
        attributeId: String,
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): QueryResults<CIAttributeListDto> {
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
                ciAttribute.attributeId.notIn(attributeId)
                    .and(ciAttribute.attributeType.notIn(RestTemplateConstants.AttributeType.GROUP_LIST.code))
                    .and(
                        super.like(ciAttribute.attributeName, ciAttributeSearchCondition.searchValue)
                            ?.or(super.like(ciAttribute.attributeType, ciAttributeSearchCondition.searchValue))
                            ?.or(super.like(ciAttribute.attributeText, ciAttributeSearchCondition.searchValue))
                            ?.or(super.like(ciAttribute.attributeDesc, ciAttributeSearchCondition.searchValue))
                    )
            ).orderBy(ciAttribute.attributeName.asc())

        if (ciAttributeSearchCondition.isPaging) {
            query.limit(ciAttributeSearchCondition.contentNumPerPage)
            query.offset((ciAttributeSearchCondition.pageNum - 1) * ciAttributeSearchCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }

    /**
     * Attribute 목록 조회 (Group List 에 포함된 속성).
     */
    override fun findAttributeListInGroupList(attributeIdList: MutableList<String>): QueryResults<CIAttributeValueDto> {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeValueDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    Expressions.asNumber(0),
                    ciAttribute.attributeValue,
                    Expressions.asString("")
                )
            )
            .where(
                ciAttribute.attributeId.`in`(attributeIdList)
            )
        return query.fetchResults()
    }

    /**
     * Group List 속성의 데이터 조회
     */
    override fun findGroupListData(attributeId: String, ciId: String): QueryResults<CIGroupListDto> {
        val cIGroupListDataEntity = QCIGroupListDataEntity.cIGroupListDataEntity
        val query = from(cIGroupListDataEntity)
            .select(
                Projections.constructor(
                    CIGroupListDto::class.java,
                    cIGroupListDataEntity.ci.ciId,
                    cIGroupListDataEntity.ciAttribute.attributeId,
                    cIGroupListDataEntity.cAttributeId,
                    cIGroupListDataEntity.cAttributeSeq,
                    cIGroupListDataEntity.cValue
                )
            ).where(
                cIGroupListDataEntity.ciAttribute.attributeId.eq(attributeId)
                    .and(cIGroupListDataEntity.ci.ciId.eq(ciId))
            )
        return query.fetchResults()
    }
}
