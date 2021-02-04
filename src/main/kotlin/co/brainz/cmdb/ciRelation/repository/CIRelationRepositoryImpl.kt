/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciRelation.repository

import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import co.brainz.cmdb.ciRelation.entity.QCIRelationEntity
import co.brainz.cmdb.provider.dto.CIRelationDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIRelationRepositoryImpl : QuerydslRepositorySupport(CIRelationEntity::class.java), CIRelationRepositoryCustom {
    override fun selectByCiId(ciId: String): MutableList<CIRelationDto> {
        val ciRelationEntity = QCIRelationEntity.cIRelationEntity

        val resultCIRelationList = from(ciRelationEntity)
            .select(
                Projections.constructor(
                    CIRelationDto::class.java,
                    ciRelationEntity.relationId,
                    ciRelationEntity.relationType,
                    ciRelationEntity.masterCIId,
                    ciRelationEntity.slaveCIId
                )
            )
            .where(
                (ciRelationEntity.masterCIId.eq(ciId))
                    ?.or(ciRelationEntity.slaveCIId.eq(ciId))
            )
            .fetchResults()

        val ciRelationList = mutableListOf<CIRelationDto>()
        for (ciRelation in resultCIRelationList.results) {
            ciRelationList.add(ciRelation)
        }

        return ciRelationList
    }
}
