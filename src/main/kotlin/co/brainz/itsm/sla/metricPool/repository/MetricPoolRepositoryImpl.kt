/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.sla.metricPool.dto.MetricPoolListDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.entity.QMetricEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricGroupEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MetricPoolRepositoryImpl(
) : QuerydslRepositorySupport(FaqEntity::class.java), MetricPoolRepositoryCustom {

    override fun findMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): PagingReturnDto {
        val metricPool = QMetricEntity.metricEntity
        val metricGroup = QMetricGroupEntity.metricGroupEntity
        val typeCode = QCodeEntity.codeEntity
        val unitCode = QCodeEntity("unitCode")
        val calcTypeCode = QCodeEntity("calcTypeCode")

        val query = from(metricPool)
            .select(
                Projections.constructor(
                    MetricPoolListDto::class.java,
                    metricPool.metricId,
                    metricGroup.metricGroupName,
                    metricPool.metricName,
                    typeCode.codeName.`as`("metricType"),
                    unitCode.codeName.`as`("metricUnit"),
                    calcTypeCode.codeName.`as`("calculationType"),
                    metricPool.metricDesc
                )
            )
            .join(metricGroup).on(metricPool.metricGroupId.eq(metricGroup.metricGroupId))
            .leftJoin(typeCode).on(metricPool.metricType.eq(typeCode.code))
            .leftJoin(unitCode).on(metricPool.metricUnit.eq(unitCode.code))
            .leftJoin(calcTypeCode).on(metricPool.calculationType.eq(calcTypeCode.code))
            .where(builder(metricPoolSearchCondition, metricPool))
            .orderBy(metricPool.createDt.desc())

        if (metricPoolSearchCondition.isPaging) {
            query.limit(metricPoolSearchCondition.contentNumPerPage)
            query.offset((metricPoolSearchCondition.pageNum - 1) * metricPoolSearchCondition.contentNumPerPage)
        }

        val countQuery = from(metricPool)
            .select(metricPool.count())
            .where(builder(metricPoolSearchCondition, metricPool))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()
        )
    }

    private fun builder(metricPoolSearchCondition: MetricPoolSearchCondition, metricPool: QMetricEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(metricPool.metricName, metricPoolSearchCondition.searchValue)
        )
        return builder
    }
}
