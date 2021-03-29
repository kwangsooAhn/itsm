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
import com.querydsl.core.QueryResults
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
                    ciClass.pClass.classId,
                    ciClass.pClass.className
                )
            )
            .rightJoin(ciClass.pClass, ciClass).on(ciClass.pClass.classId.eq(ciClass.classId))
            .where(ciClass.classId.eq(classId))
            .fetchOne()
    }

    override fun findClassList(searchDto: SearchDto): QueryResults<CIClassListDto> {
        val ciClass = QCIClassEntity.cIClassEntity
        val query = from(ciClass)
            .select(
                Projections.constructor(
                    CIClassListDto::class.java,
                    ciClass.classId,
                    ciClass.className,
                    ciClass.classDesc,
                    ciClass.classLevel,
                    ciClass.pClass.classId,
                    ciClass.pClass.className
                )
            )
            .rightJoin(ciClass.pClass, ciClass).on(ciClass.pClass.classId.eq(ciClass.classId))
            .where(
                super.like(ciClass.className, searchDto.search)
                    ?.or(super.like(ciClass.classDesc, searchDto.search))
            ).orderBy(ciClass.classLevel.asc(), ciClass.className.asc())
        if (searchDto.limit != null) {
            query.limit(searchDto.limit)
        }
        if (searchDto.offset != null) {
            query.offset(searchDto.offset)
        }
        return query.fetchResults()
    }

    override fun findClassEntityList(search: String): QueryResults<CIClassEntity> {
        val ciClass = QCIClassEntity.cIClassEntity
        return from(ciClass)
            .select(ciClass)
            .where(
                super.like(ciClass.className, search)
                    ?.or(super.like(ciClass.classDesc, search))
            ).orderBy(ciClass.classLevel.asc(), ciClass.className.asc())
            .fetchResults()
    }

    override fun findClassToAttributeList(classList: MutableList<String>): List<CIClassToAttributeDto>? {
        val ciClassAttributeMap = QCIClassAttributeMapEntity.cIClassAttributeMapEntity
        val attribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciClassAttributeMap)
            .select(
                Projections.constructor(
                    CIClassToAttributeDto::class.java,
                    ciClassAttributeMap.ciClass.classId,
                    ciClassAttributeMap.ciAttribute.attributeId,
                    ciClassAttributeMap.ciAttribute.attributeName,
                    ciClassAttributeMap.attributeOrder
                )
            )
            .innerJoin(ciClassAttributeMap.ciAttribute, attribute)
            .where(
                ciClassAttributeMap.ciClass.classId.`in`(classList)
            ).orderBy(ciClassAttributeMap.attributeOrder.asc())
        val result = query.fetchResults()
        val ciClassToAttributeList = mutableListOf<CIClassToAttributeDto>()
        for (data in result.results) {
            ciClassToAttributeList.add(data)
        }
        return ciClassToAttributeList.toList()
    }
}
