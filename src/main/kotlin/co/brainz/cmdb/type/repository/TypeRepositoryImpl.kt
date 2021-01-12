/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.type.repository

import co.brainz.cmdb.type.entity.CmdbTypeEntity
import co.brainz.cmdb.type.entity.QCmdbTypeEntity
import com.querydsl.core.QueryResults
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class TypeRepositoryImpl : QuerydslRepositorySupport(CmdbTypeEntity::class.java), TypeRepositoryCustom {
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
