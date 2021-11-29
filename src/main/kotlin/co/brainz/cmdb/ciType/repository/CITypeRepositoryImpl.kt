/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.entity.QCITypeEntity
import co.brainz.cmdb.dto.CITypeListDto
import co.brainz.cmdb.dto.SearchDto
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CITypeRepositoryImpl : QuerydslRepositorySupport(CITypeEntity::class.java), CITypeRepositoryCustom {

    override fun findType(typeId: String): CITypeListDto {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .select(
                Projections.constructor(
                    CITypeListDto::class.java,
                    ciType.typeId,
                    ciType.typeName,
                    ciType.typeDesc,
                    ciType.typeLevel,
                    ciType.typeSeq,
                    ciType.typeAlias,
                    ciType.pType.typeId,
                    ciType.pType.typeName,
                    ciType.typeIcon,
                    ciType.ciClass.classId,
                    ciType.ciClass.className
                )
            )
            .rightJoin(ciType.pType, ciType).on(ciType.pType.typeId.eq(ciType.typeId))
            .innerJoin(ciType.ciClass)
            .where(ciType.typeId.eq(typeId))
            .fetchOne()
    }

    override fun findTypeList(searchDto: SearchDto): QueryResults<CITypeListDto> {
        val ciType = QCITypeEntity.cITypeEntity
        val query = from(ciType)
            .select(
                Projections.constructor(
                    CITypeListDto::class.java,
                    ciType.typeId,
                    ciType.typeName,
                    ciType.typeDesc,
                    ciType.typeLevel,
                    ciType.typeSeq,
                    ciType.typeAlias,
                    ciType.pType.typeId,
                    ciType.pType.typeName,
                    ciType.typeIcon,
                    ciType.ciClass.classId,
                    ciType.ciClass.className
                )
            )
            .rightJoin(ciType.pType, ciType).on(ciType.pType.typeId.eq(ciType.typeId))
            .innerJoin(ciType.ciClass).fetchJoin()
            .where(
                super.likeIgnoreCase(
                    ciType.typeName, searchDto.search
                )
            )
            .orderBy(ciType.typeLevel.asc(), ciType.typeSeq.asc(), ciType.typeName.asc())
        if (searchDto.limit != null) {
            query.limit(searchDto.limit)
        }
        if (searchDto.offset != null) {
            query.offset(searchDto.offset)
        }
        return query.fetchResults()
    }

    override fun findByTypeList(search: String): QueryResults<CITypeEntity> {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .select(ciType)
            .where(
                super.likeIgnoreCase(
                    ciType.typeName, search
                )
            )
            .innerJoin(ciType.ciClass).fetchJoin()
            .orderBy(ciType.typeLevel.asc(), ciType.typeSeq.asc(), ciType.typeName.asc())
            .fetchResults()
    }

    override fun findByCITypeAll(): List<CITypeEntity>? {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .innerJoin(ciType.ciClass).fetchJoin()
            .orderBy(ciType.typeLevel.asc(), ciType.typeSeq.asc())
            .fetch()
    }
}
