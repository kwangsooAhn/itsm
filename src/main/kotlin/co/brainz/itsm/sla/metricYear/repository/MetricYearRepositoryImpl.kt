/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricEntity
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
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
    }

    override fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        val metric = QMetricEntity.metricEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val typeCode = QCodeEntity.codeEntity
        val unitCode = QCodeEntity("unitCode")
        val calcTypeCode = QCodeEntity("calcTypeCode")
        val groupCode = QCodeEntity("groupCode")

        val query = from(metric)
            .select(
                Projections.constructor(
                    MetricLoadDto::class.java,
                    metric.metricId,
                    metricYear.metricYear,
                    metric.metricName,
                    groupCode.codeName,
                    typeCode.code,
                    unitCode.code,
                    calcTypeCode.code
                )
            )
            .leftJoin(metricYear).on(metric.metricId.eq(metricYear.metric.metricId))
            .leftJoin(groupCode).on(metric.metricGroup.eq(groupCode.code))
            .leftJoin(unitCode).on(metric.metricUnit.eq(unitCode.code))
            .leftJoin(typeCode).on(metric.metricType.eq(typeCode.code))
            .leftJoin(calcTypeCode).on(metric.calculationType.eq(calcTypeCode.code))
            .where(metricYear.metricYear.`in`(metricLoadCondition.sourceYear))

        if (metricLoadCondition.targetYear!!.isNotEmpty()) {
            query.where(metricYear.metricYear.notIn(metricLoadCondition.targetYear))
        }
        if (metricLoadCondition.metricType!!.isNotEmpty()) {
            query.where(metric.metricType.`in`(metricLoadCondition.metricType))
        }

        return query.fetch()
    }
}
