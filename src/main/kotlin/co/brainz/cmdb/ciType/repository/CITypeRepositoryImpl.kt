package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.entity.QCITypeEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CITypeRepositoryImpl : QuerydslRepositorySupport(CITypeEntity::class.java), CITypeRepositoryCustom {
    override fun findByTypeList(search: String): QueryResults<CITypeEntity> {
        val type = QCITypeEntity.cITypeEntity
        return from(type)
            .select(type)
            .where(
                super.likeIgnoreCase(
                    type.typeName, search
                )
            )
            .orderBy(type.typeLevel.asc(), type.typeName.asc())
            .fetchResults()
    }

    override fun findByCITypeAll(): List<CITypeEntity>? {
        val type = QCITypeEntity.cITypeEntity
        return from(type)
            .orderBy(type.typeLevel.asc())
            .fetch()
    }
}
