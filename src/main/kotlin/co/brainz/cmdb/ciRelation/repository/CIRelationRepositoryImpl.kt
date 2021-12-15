/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciRelation.repository

import co.brainz.cmdb.ci.entity.QCIEntity
import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import co.brainz.cmdb.ciRelation.entity.QCIRelationEntity
import co.brainz.cmdb.dto.CIRelationDto
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIRelationRepositoryImpl : QuerydslRepositorySupport(CIRelationEntity::class.java), CIRelationRepositoryCustom {
    override fun selectByCiId(ciId: String): MutableList<CIRelationDto> {
        val ciRelation = QCIRelationEntity.cIRelationEntity
        val ci = QCIEntity.cIEntity
        return from(ciRelation)
            .select(
                Projections.constructor(
                    CIRelationDto::class.java,
                    ciRelation.relationId,
                    ciRelation.ciId,
                    ciRelation.targetCIId,
                    ci.ciName,
                    ci.ciTypeEntity.typeIcon,
                    ci.ciTypeEntity.typeName
                )
            )
            .innerJoin(ci).on(ci.ciId.eq(ciRelation.ciId))
            .where(ciRelation.ciId.eq(ciId))
            .fetch()
    }
}
