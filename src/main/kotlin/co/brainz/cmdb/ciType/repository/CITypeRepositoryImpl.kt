package co.brainz.cmdb.ciType.repository

import co.brainz.cmdb.ciType.entity.CmdbTypeEntity
import co.brainz.cmdb.ciType.entity.QCmdbTypeEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CITypeRepositoryImpl : QuerydslRepositorySupport(CmdbTypeEntity::class.java), CITypeRepositoryCustom {
    override fun findByTypeList(search: String): QueryResults<CmdbTypeEntity> {
        val type = QCmdbTypeEntity.cmdbTypeEntity
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
}
