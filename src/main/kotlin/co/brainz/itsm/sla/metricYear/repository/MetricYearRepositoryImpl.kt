/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricPoolEntity
import co.brainz.itsm.sla.metricYear.dto.MetricYearDataDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.QMetricYearEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MetricYearRepositoryImpl : QuerydslRepositorySupport(MetricYearEntity::class.java), MetricYearRepositoryCustom {

    override fun existsByMetric(metricId: String): Boolean {
        val metricYear = QMetricYearEntity.metricYearEntity
        return from(metricYear)
            .where(metricYear.metric.metricId.eq(metricId))
            .fetchFirst() != null
    }

    override fun findMetrics(metricYearSearchCondition: MetricYearSearchCondition): PagingReturnDto {
        val content = this.getMetrics(metricYearSearchCondition)
        val count = this.getMetricsCount(metricYearSearchCondition)

        return PagingReturnDto(
            dataList = content.fetch(),
            totalCount = count.fetchOne()
        )
    }

    private fun getMetrics(metricYearSearchCondition: MetricYearSearchCondition): JPQLQuery<MetricYearDataDto> {
        val metricPool = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val code = QCodeEntity.codeEntity

        val query = from(metricPool)
            .select(
                Projections.constructor(
                    MetricYearDataDto::class.java,
                    metricPool.metricId,
                    code.codeName.`as`("metricGroupName"),
                    metricPool.metricName,
                    metricYear.minValue,
                    metricYear.maxValue,
                    metricYear.weightValue,
                    metricYear.owner,
                    metricYear.comment
                )
            )
            .join(metricYear).on(metricPool.metricId.eq(metricYear.metric.metricId))
            .leftJoin(code).on(metricPool.metricGroup.eq(code.code))
            .where(builder(metricYearSearchCondition, metricYear))
            .orderBy(metricYear.createDt.desc())

        if (metricYearSearchCondition.isPaging) {
            query.limit(metricYearSearchCondition.contentNumPerPage)
            query.offset((metricYearSearchCondition.pageNum - 1) * metricYearSearchCondition.contentNumPerPage)
        }
        return query
    }

    private fun getMetricsCount(metricYearSearchCondition: MetricYearSearchCondition): JPQLQuery<Long> {
        val metricPool = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        return from(metricPool)
            .select(metricPool.count())
            .join(metricYear).on(metricPool.metricId.eq(metricYear.metric.metricId))
            .where(builder(metricYearSearchCondition, metricYear))
    }

    private fun builder(metricYearSearchCondition: MetricYearSearchCondition, metricYear: QMetricYearEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(metricYear.metricYear, metricYearSearchCondition.year)
        )
        return builder
    }
}
