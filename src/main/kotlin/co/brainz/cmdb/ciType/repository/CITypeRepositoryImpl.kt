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
import co.brainz.framework.querydsl.dto.PagingReturnDto
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

    override fun findTypeList(searchDto: SearchDto): PagingReturnDto {
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

        val countQuery = from(ciType)
            .select(ciType.count())
            .where(super.likeIgnoreCase(ciType.typeName, searchDto.search))
        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    override fun findByTypeList(search: String): List<CITypeEntity> {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .select(ciType)
            .where(
                super.likeIgnoreCase(
                    ciType.typeName, search
                )
            )
            .orderBy(ciType.typeLevel.asc(), ciType.typeSeq.asc(), ciType.typeName.asc())
            .fetch()
    }

    override fun findByCITypeAll(): List<CITypeEntity>? {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .innerJoin(ciType.ciClass).fetchJoin()
            .orderBy(ciType.typeLevel.asc(), ciType.typeSeq.asc())
            .fetch()
    }

    override fun findCITypeByTypeName(typeName: String): CITypeEntity? {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .where(ciType.typeName.eq(typeName))
            .fetchOne()
    }
}
