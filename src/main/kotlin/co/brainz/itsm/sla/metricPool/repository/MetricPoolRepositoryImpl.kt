/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.dto.MetricData
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.dto.MetricViewData
import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricPoolEntity
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
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

    private fun getMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): JPQLQuery<MetricViewData> {
        val metric = QMetricPoolEntity.metricPoolEntity
        val typeCode = QCodeEntity.codeEntity
        val unitCode = QCodeEntity("unitCode")
        val calcTypeCode = QCodeEntity("calcTypeCode")
        val groupCode = QCodeEntity("groupCode")

        val query = from(metric)
            .select(
                Projections.constructor(
                    MetricViewData::class.java,
                    metric.metricId,
                    metric.metricName,
                    metric.metricDesc,
                    groupCode.codeName.`as`("metricGroupName"),
                    typeCode.codeName.`as`("metricTypeName"),
                    unitCode.codeName.`as`("metricUnitName"),
                    calcTypeCode.codeName.`as`("calculationTypeName")
                )
            )
            .leftJoin(typeCode).on(metric.metricType.eq(typeCode.code))
            .leftJoin(unitCode).on(metric.metricUnit.eq(unitCode.code))
            .leftJoin(calcTypeCode).on(metric.calculationType.eq(calcTypeCode.code))
            .leftJoin(groupCode).on(metric.metricGroup.eq(groupCode.code))
            .where(this.searchByBuilder(metricPoolSearchCondition, metric))
            .orderBy(metric.createDt.desc())

        if (metricPoolSearchCondition.isPaging) {
            query.limit(metricPoolSearchCondition.contentNumPerPage)
            query.offset((metricPoolSearchCondition.pageNum - 1) * metricPoolSearchCondition.contentNumPerPage)
        }
        return query
    }

    private fun getMetricPoolsCount(metricPoolSearchCondition: MetricPoolSearchCondition): JPQLQuery<Long> {
        val metric = QMetricPoolEntity.metricPoolEntity
        return from(metric)
            .select(metric.count())
            .where(this.searchByBuilder(metricPoolSearchCondition, metric))
    }

    override fun findMetric(metricId: String): MetricData {
        val metric = QMetricPoolEntity.metricPoolEntity

        return from(metric)
            .select(
                Projections.constructor(
                    MetricData::class.java,
                    metric.metricId,
                    metric.metricName,
                    metric.metricDesc,
                    metric.metricGroup,
                    metric.metricType,
                    metric.metricUnit,
                    metric.calculationType
                )
            )
            .where(metric.metricId.eq(metricId))
            .fetchOne()
    }

    private fun searchByBuilder(metricPoolSearchCondition: MetricPoolSearchCondition, metricPool: QMetricPoolEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(metricPool.metricName, metricPoolSearchCondition.searchValue)
        )
        return builder
    }

    override fun findByMetricIds(metricIds: Set<String>): List<MetricLoadDto> {
        val metric = QMetricPoolEntity.metricPoolEntity
        val typeCode = QCodeEntity.codeEntity
        val unitCode = QCodeEntity("unitCode")
        val calcTypeCode = QCodeEntity("calcTypeCode")
        val groupCode = QCodeEntity("groupCode")
        return from(metric)
            .select(
                Projections.constructor(
                    MetricLoadDto::class.java,
                    metric.metricId,
                    metric.metricName,
                    metric.metricDesc,
                    groupCode.codeName,
                    typeCode.code,
                    unitCode.code,
                    calcTypeCode.code
                )
            )
            .leftJoin(groupCode).on(metric.metricGroup.eq(groupCode.code))
            .leftJoin(unitCode).on(metric.metricUnit.eq(unitCode.code))
            .leftJoin(typeCode).on(metric.metricType.eq(typeCode.code))
            .leftJoin(calcTypeCode).on(metric.calculationType.eq(calcTypeCode.code))
            .where(metric.metricId.`in`(metricIds))
            .fetch()
    }
}
