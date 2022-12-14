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
import co.brainz.cmdb.dto.CISearchItem
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.cmdb.ciAttribute.dto.CIAttributeSearchCondition
import com.querydsl.core.BooleanBuilder
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
    override fun findAttributeList(ciAttributeSearchCondition: CIAttributeSearchCondition): PagingReturnDto {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeListDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    ciAttribute.attributeDesc,
                    ciAttribute.searchYn,
                    ciAttribute.searchWidth
                )
            )
            .where(builder(ciAttributeSearchCondition, ciAttribute, null))
            .orderBy(ciAttribute.attributeName.asc())

        if (ciAttributeSearchCondition.isPaging) {
            query.limit(ciAttributeSearchCondition.contentNumPerPage)
            query.offset((ciAttributeSearchCondition.pageNum - 1) * ciAttributeSearchCondition.contentNumPerPage)
        }

        val countQuery = from(ciAttribute)
            .select(ciAttribute.count())
            .where(builder(ciAttributeSearchCondition, ciAttribute, null))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findAttributeList(attributeIds: Set<String>): List<CISearchItem> {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        return from(ciAttribute)
            .select(
                Projections.constructor(
                    CISearchItem::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    ciAttribute.attributeDesc,
                    ciAttribute.attributeValue,
                    Expressions.asString("")
                )
            )
            .where(ciAttribute.attributeId.`in`(attributeIds))
            .fetch()
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
                    ciAttribute.attributeDesc,
                    ciAttribute.searchYn,
                    ciAttribute.searchWidth
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
                    ciAttribute.searchYn,
                    ciAttribute.searchWidth,
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
            .select(ciAttribute.count())
            .where(ciAttribute.attributeName.eq(attributeName))
        if (attributeId.isNotEmpty()) {
            query.where(!ciAttribute.attributeId.eq(attributeId))
        }
        return query.fetchOne()
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
                    ciAttributeEntity.attributeDesc,
                    ciAttributeEntity.attributeText,
                    ciAttributeEntity.attributeType,
                    ciClassAttributeMapEntity.attributeOrder,
                    ciAttributeEntity.attributeValue,
                    ciAttributeEntity.searchYn,
                    ciAttributeEntity.searchWidth,
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

        return query.fetch()
    }

    /**
     * Attribute 목록 조회 (Group List 제외).
     */
    override fun findAttributeListWithoutGroupList(
        attributeId: String,
        ciAttributeSearchCondition: CIAttributeSearchCondition
    ): PagingReturnDto {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeListDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    ciAttribute.attributeDesc,
                    ciAttribute.searchYn,
                    ciAttribute.searchWidth
                )
            )
            .where(builder(ciAttributeSearchCondition, ciAttribute, attributeId))
            .orderBy(ciAttribute.attributeName.asc())
        if (ciAttributeSearchCondition.isPaging) {
            query.limit(ciAttributeSearchCondition.contentNumPerPage)
            query.offset((ciAttributeSearchCondition.pageNum - 1) * ciAttributeSearchCondition.contentNumPerPage)
        }

        val countQuery = from(ciAttribute)
            .select(ciAttribute.count())
            .where(builder(ciAttributeSearchCondition, ciAttribute, attributeId))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    /**
     * Attribute 목록 조회 (Group List 에 포함된 속성).
     */
    override fun findAttributeListInGroupList(attributeIdList: MutableList<String>): List<CIAttributeValueDto> {
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciAttribute)
            .select(
                Projections.constructor(
                    CIAttributeValueDto::class.java,
                    ciAttribute.attributeId,
                    ciAttribute.attributeName,
                    ciAttribute.attributeDesc,
                    ciAttribute.attributeText,
                    ciAttribute.attributeType,
                    Expressions.asNumber(0),
                    ciAttribute.attributeValue,
                    ciAttribute.searchYn,
                    ciAttribute.searchWidth,
                    Expressions.asString("")
                )
            )
            .where(
                ciAttribute.attributeId.`in`(attributeIdList)
            )
        return query.fetch()
    }

    /**
     * Group List 속성의 데이터 조회
     */
    override fun findGroupListData(attributeId: String, ciId: String): List<CIGroupListDto> {
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
        return query.fetch()
    }

    private fun builder(
        ciAttributeSearchCondition: CIAttributeSearchCondition,
        ciAttribute: QCIAttributeEntity,
        attributeId: String?
    ): BooleanBuilder {
        val builder = BooleanBuilder()

        if (attributeId != null) {
            builder.and(
                ciAttribute.attributeId.notIn(attributeId)
                    .and(ciAttribute.attributeType.notIn(RestTemplateConstants.AttributeType.GROUP_LIST.code))
            )
        }
        builder.and(
            super.likeIgnoreCase(ciAttribute.attributeName, ciAttributeSearchCondition.searchValue)
                ?.or(super.likeIgnoreCase(ciAttribute.attributeType, ciAttributeSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(ciAttribute.attributeText, ciAttributeSearchCondition.searchValue))
                ?.or(super.likeIgnoreCase(ciAttribute.attributeDesc, ciAttributeSearchCondition.searchValue))
        )
        return builder
    }
}
