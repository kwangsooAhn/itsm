/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.dto.MetricDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricPoolEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MetricPoolRepositoryImpl : QuerydslRepositorySupport(MetricPoolEntity::class.java), MetricPoolRepositoryCustom {

    override fun findMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): PagingReturnDto {
        val content = this.getMetricPools(metricPoolSearchCondition)
        val count = this.getMetricPoolsCount(metricPoolSearchCondition)
        return PagingReturnDto(
            dataList = content.fetch(),
            totalCount = count.fetchOne()
        )
    }

    private fun getMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): JPQLQuery<MetricPoolDto> {
        val metricPool = QMetricPoolEntity.metricPoolEntity
        val typeCode = QCodeEntity.codeEntity
        val unitCode = QCodeEntity("unitCode")
        val calcTypeCode = QCodeEntity("calcTypeCode")
        val groupCode = QCodeEntity("groupCode")

        val query = from(metricPool)
            .select(
                Projections.constructor(
                    MetricPoolDto::class.java,
                    metricPool.metricId,
                    metricPool.metricName,
                    metricPool.metricDesc,
                    groupCode.codeName.`as`("metricGroupName"),
                    typeCode.codeName.`as`("metricTypeName"),
                    unitCode.codeName.`as`("metricUnitName"),
                    calcTypeCode.codeName.`as`("calculationTypeName")
                )
            )
            .leftJoin(typeCode).on(metricPool.metricType.eq(typeCode.code))
            .leftJoin(unitCode).on(metricPool.metricUnit.eq(unitCode.code))
            .leftJoin(calcTypeCode).on(metricPool.calculationType.eq(calcTypeCode.code))
            .leftJoin(groupCode).on(metricPool.metricGroup.eq(groupCode.code))
            .where(builder(metricPoolSearchCondition, metricPool))
            .orderBy(metricPool.createDt.desc())

        if (metricPoolSearchCondition.isPaging) {
            query.limit(metricPoolSearchCondition.contentNumPerPage)
            query.offset((metricPoolSearchCondition.pageNum - 1) * metricPoolSearchCondition.contentNumPerPage)
        }
        return query
    }

    private fun getMetricPoolsCount(metricPoolSearchCondition: MetricPoolSearchCondition): JPQLQuery<Long> {
        val metricPool = QMetricPoolEntity.metricPoolEntity
        return from(metricPool)
            .select(metricPool.count())
            .where(builder(metricPoolSearchCondition, metricPool))
    }

    override fun findMetric(metricId: String): MetricDto {
        val metricPool = QMetricPoolEntity.metricPoolEntity

        return from(metricPool)
            .select(
                Projections.constructor(
                    MetricDto::class.java,
                    metricPool.metricId,
                    metricPool.metricName,
                    metricPool.metricDesc,
                    metricPool.metricGroup,
                    metricPool.metricType,
                    metricPool.metricUnit,
                    metricPool.calculationType
                )
            )
            .where(metricPool.metricId.eq(metricId))
            .fetchOne()
    }

    private fun builder(metricPoolSearchCondition: MetricPoolSearchCondition, metricPool: QMetricPoolEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(metricPool.metricName, metricPoolSearchCondition.searchValue)
        )
        return builder
    }
}
