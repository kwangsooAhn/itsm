/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricPool.entity.QMetricEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricGroupEntity
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.QMetricYearEntity
import com.querydsl.core.types.Projections
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

    override fun findMetricYearList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        val metric = QMetricEntity.metricEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val metricGroup = QMetricGroupEntity.metricGroupEntity
        val code = QCodeEntity.codeEntity
        val query = from(metric)
            .select(
                Projections.constructor(
                    MetricLoadDto::class.java,
                    metric.metricId,
                    metricYear.metricYear,
                    metric.metricName,
                    metricGroup.metricGroupName,
                    code.codeName,
                    code.codeName,
                    code.codeName
                )
            )
            .leftJoin(metricYear).on(metric.metricId.eq(metricYear.metric.metricId))
            .leftJoin(metricGroup).on(metric.metricGroupId.eq(metricGroup.metricGroupId))
            .leftJoin(code).on(metric.metricUnit.eq(code.code))
            .leftJoin(code).on(metric.metricType.eq(code.code))
            .leftJoin(code).on(metric.calculationType.eq(code.code))
            .where(metricYear.metricYear.`in`(metricLoadCondition.targetYear))
        if (metricLoadCondition.sourceYear != null) {
            query.where(metricYear.metricYear.notIn(metricLoadCondition.sourceYear))
        }
        if (metricLoadCondition.metricType!!.isNotEmpty()) {
            query.where(metric.metricType.`in`(metricLoadCondition.metricType))
        }

        return query.fetch()
    }
}
