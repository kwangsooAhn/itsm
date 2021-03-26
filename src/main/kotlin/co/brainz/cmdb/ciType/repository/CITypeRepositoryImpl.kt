package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.entity.QCITypeEntity
import co.brainz.cmdb.dto.SearchDto
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CITypeRepositoryImpl : QuerydslRepositorySupport(CITypeEntity::class.java), CITypeRepositoryCustom {

    override fun findType(typeId: String): CITypeEntity? {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .where(ciType.typeId.eq(typeId))
            .fetchOne()
    }

    override fun findTypeList(searchDto: SearchDto): QueryResults<CITypeEntity> {
        val ciType = QCITypeEntity.cITypeEntity
        val query = from(ciType)
            .where(
                super.like(
                    ciType.typeName, searchDto.search
                )
            )
            .orderBy(ciType.typeLevel.asc(), ciType.typeName.asc())
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
                super.like(
                    ciType.typeName, search
                )
            )
            .orderBy(ciType.typeLevel.asc(), ciType.typeName.asc())
            .fetchResults()
    }

    override fun findByCITypeAll(): List<CITypeEntity>? {
        val ciType = QCITypeEntity.cITypeEntity
        return from(ciType)
            .orderBy(ciType.typeLevel.asc())
            .fetch()
    }
}
