/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ciRelation.repository

import co.brainz.cmdb.ci.entity.QCIEntity
import co.brainz.cmdb.ciRelation.entity.CIRelationEntity
import co.brainz.cmdb.ciRelation.entity.QCIRelationEntity
import co.brainz.cmdb.dto.CIRelationDto
import co.brainz.itsm.code.entity.QCodeEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIRelationRepositoryImpl : QuerydslRepositorySupport(CIRelationEntity::class.java), CIRelationRepositoryCustom {
    override fun selectByCiId(ciId: String): MutableList<CIRelationDto> {
        val ciRelation = QCIRelationEntity.cIRelationEntity
        val sourceCI = QCIEntity.cIEntity
        val targetCI = QCIEntity("targetCI")
        val code = QCodeEntity.codeEntity

        return from(ciRelation)
            .select(
                Projections.constructor(
                    CIRelationDto::class.java,
                    ciRelation.relationId,
                    ciRelation.relationType,
                    code.codeName,
                    ciRelation.sourceCIId,
                    sourceCI.ciName,
                    ciRelation.targetCIId,
                    targetCI.ciName
                )
            )
            .innerJoin(sourceCI).on(sourceCI.ciId.eq(ciRelation.sourceCIId))
            .innerJoin(targetCI).on(targetCI.ciId.eq(ciRelation.targetCIId))
            .innerJoin(code).on(
                code.codeValue.eq(ciRelation.relationType)
                .and(
                    code.pCode.code.eq("cmdb.relation.type")
                )
            )
            .where(
                (ciRelation.sourceCIId.eq(ciId))
                    ?.or(ciRelation.targetCIId.eq(ciId))
            )
            .fetch()
    }
}
