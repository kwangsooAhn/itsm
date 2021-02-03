/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ciTag.repository

import co.brainz.cmdb.ciTag.entity.QCITagEntity
import co.brainz.cmdb.ciTag.entity.CITagEntity
import co.brainz.cmdb.provider.dto.CITagDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CITagRepositoryImpl : QuerydslRepositorySupport(CITagEntity::class.java), CITagRepositoryCustom {

    /**
     * CI Tag 목록 조회.
     */
    override fun findByCIId(search: String): MutableList<CITagDto> {
        val ciTag = QCITagEntity.cITagEntity
        val query = from(ciTag)
            .select(
                Projections.constructor(
                    CITagDto::class.java,
                    ciTag.ci.ciId,
                    ciTag.tagId,
                    ciTag.tagName
                )
            )
            .where(
                super.eq(ciTag.ci.ciId, search)
            ).orderBy(ciTag.tagName.asc())
        val result = query.fetchResults()
        val tags = mutableListOf<CITagDto>()
        for (data in result.results) {
            tags.add(data)
        }
        return tags
    }
}
