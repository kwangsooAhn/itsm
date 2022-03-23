/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIGroupListDataEntity
import co.brainz.cmdb.ci.entity.QCIGroupListDataEntity
import co.brainz.cmdb.ciAttribute.entity.QCIAttributeEntity
import co.brainz.cmdb.dto.CIGroupListDataDto
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIGroupListDataRepositoryImpl : QuerydslRepositorySupport(CIGroupListDataEntity::class.java),
    CIGroupListDataRepositoryCustom {
    override fun getCIGroupListDataList(
        ciIds: Set<String>,
        attributeId: String,
        cAttributeIds: Set<String>
    ): List<CIGroupListDataDto> {
        val ciGroupListData = QCIGroupListDataEntity.cIGroupListDataEntity
        val ciAttribute = QCIAttributeEntity.cIAttributeEntity
        val query = from(ciGroupListData)
            .select(
                Projections.constructor(
                    CIGroupListDataDto::class.java,
                    ciGroupListData.ci.ciId,
                    ciGroupListData.ciAttribute.attributeId,
                    ciGroupListData.cAttributeId,
                    ciGroupListData.cAttributeSeq,
                    ciGroupListData.cValue,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(ciAttribute.attributeText).from(ciAttribute)
                            .where(ciAttribute.attributeId.eq(ciGroupListData.cAttributeId)), "cAttributeText"
                    )
                )
            )
            .where(ciGroupListData.ci.ciId.`in`(ciIds))
            .where(ciGroupListData.ciAttribute.attributeId.eq(attributeId))
            .where(ciGroupListData.cAttributeId.`in`(cAttributeIds))
            .orderBy(ciGroupListData.cAttributeSeq.asc(), ciGroupListData.cAttributeId.asc())
        return query.fetch()
    }
}
