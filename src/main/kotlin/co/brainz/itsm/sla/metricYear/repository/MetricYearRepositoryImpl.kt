/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricEntity
import co.brainz.itsm.sla.metricPool.repository.MetricYearRepositoryCustom
import co.brainz.itsm.sla.metricYear.dto.MetricSearchCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearDto
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.QMetricYearEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MetricYearRepositoryImpl(
) : QuerydslRepositorySupport(MetricYearEntity::class.java), MetricYearRepositoryCustom {

    override fun existsByMetric(metricId: String): Boolean {
        val metricYear = QMetricYearEntity.metricYearEntity
        return from(metricYear)
            .where(metricYear.metric.metricId.eq(metricId))
            .fetchFirst() != null
    }

    override fun findMetrics(metricSearchCondition: MetricSearchCondition): PagingReturnDto {
        val content = this.getMetrics(metricSearchCondition)
        val count = this.getMetricsCount(metricSearchCondition)

        return PagingReturnDto(
            dataList = content.fetch(),
            totalCount = count.fetchOne()
        )
    }

    private fun getMetrics(metricSearchCondition: MetricSearchCondition): JPQLQuery<MetricYearDto> {
        val metricPool = QMetricEntity.metricEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val code = QCodeEntity.codeEntity

        val query = from(metricPool)
            .select(
                Projections.constructor(
                    MetricYearDto::class.java,
                    metricPool.metricId

                )
            )
            .join(metricYear).on(metricPool.metricId.eq(metricYear.metric.metricId))
            .leftJoin(code).on(metricPool.metricGroup.eq(code.code))
            .where(builder(metricSearchCondition, metricYear))
            .orderBy(metricYear.createDt.desc())

        if (metricSearchCondition.isPaging) {
            query.limit(metricSearchCondition.contentNumPerPage)
            query.offset((metricSearchCondition.pageNum - 1) * metricSearchCondition.contentNumPerPage)
        }
        return query
    }

    private fun getMetricsCount(metricSearchCondition: MetricSearchCondition): JPQLQuery<Long> {
        val metricPool = QMetricEntity.metricEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        return from(metricPool)
            .select(metricPool.count())
            .where(builder(metricSearchCondition, metricYear))
    }

    private fun builder(metricSearchCondition: MetricSearchCondition, metricYear: QMetricYearEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(metricYear.metricYear, metricSearchCondition.year)
        )
        return builder
    }
}
