/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciClass.repository

import co.brainz.cmdb.ciAttribute.entity.QCIAttributeEntity
import co.brainz.cmdb.ciClass.entity.CIClassEntity
import co.brainz.cmdb.ciClass.entity.QCIClassAttributeMapEntity
import co.brainz.cmdb.ciClass.entity.QCIClassEntity
import co.brainz.cmdb.dto.CIClassListDto
import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.cmdb.dto.SearchDto
import co.brainz.framework.querydsl.dto.PagingReturnDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIClassRepositoryImpl : QuerydslRepositorySupport(CIClassEntity::class.java), CIClassRepositoryCustom {

    override fun findClass(classId: String): CIClassListDto? {
        val ciClass = QCIClassEntity.cIClassEntity
        return from(ciClass)
            .select(
                Projections.constructor(
                    CIClassListDto::class.java,
                    ciClass.classId,
                    ciClass.className,
                    ciClass.classDesc,
                    ciClass.classLevel,
                    ciClass.classSeq,
                    ciClass.pClass.classId,
                    ciClass.pClass.className
                )
            )
            .rightJoin(ciClass.pClass, ciClass).on(ciClass.pClass.classId.eq(ciClass.classId))
            .where(ciClass.classId.eq(classId))
            .fetchOne()
    }

    override fun findClassList(searchDto: SearchDto): PagingReturnDto {
        val ciClass = QCIClassEntity.cIClassEntity
        val query = from(ciClass)
            .select(
                Projections.constructor(
                    CIClassListDto::class.java,
                    ciClass.classId,
                    ciClass.className,
                    ciClass.classDesc,
                    ciClass.classLevel,
                    ciClass.classSeq,
                    ciClass.pClass.classId,
                    ciClass.pClass.className
                )
            )
            .rightJoin(ciClass.pClass, ciClass).on(ciClass.pClass.classId.eq(ciClass.classId))
            .where(
                super.likeIgnoreCase(ciClass.className, searchDto.search)
                    ?.or(super.likeIgnoreCase(ciClass.classDesc, searchDto.search))
            ).orderBy(ciClass.classLevel.asc(), ciClass.classSeq.asc(), ciClass.className.asc())
        if (searchDto.limit != null) {
            query.limit(searchDto.limit)
        }
        if (searchDto.offset != null) {
            query.offset(searchDto.offset)
        }

        val countQuery = from(ciClass)
            .select(ciClass.count())
            .where(
                super.likeIgnoreCase(ciClass.className, searchDto.search)
                    ?.or(super.likeIgnoreCase(ciClass.classDesc, searchDto.search))
            )

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findClassEntityList(search: String): List<CIClassEntity> {
        val ciClass = QCIClassEntity.cIClassEntity
        return from(ciClass)
            .select(ciClass)
            .where(
                super.likeIgnoreCase(ciClass.className, search)
                    ?.or(super.likeIgnoreCase(ciClass.classDesc, search))
            ).orderBy(ciClass.classLevel.asc(), ciClass.classSeq.asc(), ciClass.className.asc())
            .fetch()
    }

    override fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>? {
        val ciClassAttributeMap = QCIClassAttributeMapEntity.cIClassAttributeMapEntity
        val attribute = QCIAttributeEntity.cIAttributeEntity
        val ciClass = QCIClassEntity.cIClassEntity
        val query = from(ciClassAttributeMap)
            .select(
                Projections.constructor(
                    CIClassToAttributeDto::class.java,
                    ciClassAttributeMap.ciClass.classId,
                    ciClassAttributeMap.ciAttribute.attributeId,
                    ciClassAttributeMap.ciAttribute.attributeName,
                    ciClassAttributeMap.ciAttribute.attributeText,
                    ciClassAttributeMap.ciAttribute.attributeType,
                    ciClass.classLevel,
                    ciClassAttributeMap.attributeOrder
                )
            )
            .innerJoin(ciClassAttributeMap.ciClass, ciClass)
            .innerJoin(ciClassAttributeMap.ciAttribute, attribute)
            .where(
                ciClassAttributeMap.ciClass.classId.`in`(classList)
            )
            .orderBy(ciClass.classLevel.asc(), ciClassAttributeMap.attributeOrder.asc())
        val ciClassToAttributeList = mutableListOf<CIClassToAttributeDto>()
        for (data in query.fetch()) {
            ciClassToAttributeList.add(data)
        }
        return ciClassToAttributeList.toList()
    }
}
