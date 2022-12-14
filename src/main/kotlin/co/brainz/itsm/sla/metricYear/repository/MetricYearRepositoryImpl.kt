/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricPoolEntity
import co.brainz.itsm.sla.metricYear.dto.MetricAnnualDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearDetailDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSimpleDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearViewData
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.QMetricYearEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
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

    override fun findMetrics(year: String): List<MetricYearViewData> {
        val metricPool = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val code = QCodeEntity.codeEntity

        return from(metricPool)
            .select(
                Projections.constructor(
                    MetricYearViewData::class.java,
                    metricPool.metricId,
                    metricYear.metricYear,
                    code.codeName.`as`("metricGroupName"),
                    metricPool.metricName,
                    metricPool.metricUnit,
                    metricYear.minValue,
                    metricYear.maxValue,
                    metricYear.weightValue,
                    metricYear.owner,
                    metricYear.comment
                )
            )
            .join(metricYear).on(metricPool.metricId.eq(metricYear.metric.metricId))
            .leftJoin(code).on(metricPool.metricGroup.eq(code.code))
            .where(super.likeIgnoreCase(metricYear.metricYear, year))
            .orderBy(metricYear.createDt.desc())
            .fetch()
    }

    override fun existsByMetricAndMetricYear(metricId: String, metricYear: String): Boolean {
        val metricYears = QMetricYearEntity.metricYearEntity
        return from(metricYears)
            .where(
                metricYears.metric.metricId.eq(metricId)
                    .and(metricYears.metricYear.eq(metricYear))
            )
            .fetchFirst() != null
    }

    override fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricYearSimpleDto> {
        val metric = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val unitCode = QCodeEntity("unitCode")

        val query = from(metric)
            .select(
                Projections.constructor(
                    MetricYearSimpleDto::class.java,
                    metric.metricId,
                    metricYear.metricYear,
                    metric.metricName,
                    unitCode.code
                )
            )
            .leftJoin(metricYear).on(metric.metricId.eq(metricYear.metric.metricId))
            .leftJoin(unitCode).on(metric.metricUnit.eq(unitCode.code))
        if (!metricLoadCondition.source.isNullOrEmpty()) {
            query.where(metricYear.metricYear.eq(metricLoadCondition.source))
        }
        if (!metricLoadCondition.target.isNullOrEmpty()) {
            query.where(metricYear.metricYear.isNull.or(metricYear.metricYear.ne(metricLoadCondition.target)))
        }
        if (!metricLoadCondition.type.isNullOrEmpty()) {
            query.where(metric.metricType.eq(metricLoadCondition.type))
        }

        return query.fetch()
    }

    override fun findMetricYearListByLoadCondition(year: String): List<MetricYearSimpleDto> {
        val metric = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity

        return from(metricYear)
            .select(
                Projections.constructor(
                    MetricYearSimpleDto::class.java,
                    metric.metricId,
                    metricYear.metricYear,
                    metric.metricName,
                    metric.metricUnit
                )
            )
            .leftJoin(metric).on(metricYear.metric.metricId.eq(metric.metricId))
            .where(metricYear.metricYear.eq(year))
            .fetch()
    }

    override fun findMetricYear(metricId: String, year: String): MetricYearDetailDto? {
        val metric = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val groupCode = QCodeEntity.codeEntity

        return from(metric)
            .select(
                Projections.constructor(
                    MetricYearDetailDto::class.java,
                    metricYear.metric.metricId,
                    metricYear.metricYear,
                    groupCode.codeName.`as`("metricGroupName"),
                    metric.metricName,
                    metric.metricDesc,
                    metric.metricType,
                    metric.metricUnit,
                    metric.calculationType,
                    metricYear.minValue,
                    metricYear.maxValue,
                    metricYear.weightValue,
                    metricYear.owner,
                    metricYear.comment,
                    metricYear.zqlString
                )
            )
            .join(metricYear).on(metric.metricId.eq(metricYear.metric.metricId))
            .leftJoin(groupCode).on(metric.metricGroup.eq(groupCode.code))
            .where(
                metricYear.metric.metricId.eq(metricId)
                    .and(metricYear.metricYear.eq(year))
            )
            .fetchOne()
    }

    override fun getYears(): Set<String> {
        val metricYear = QMetricYearEntity.metricYearEntity

        return from(metricYear).distinct()
            .select(metricYear.metricYear)
            .orderBy(metricYear.metricYear.desc())
            .fetch().toSet()
    }

    override fun findMetricStatusList(year: String): List<MetricAnnualDto> {
        val metricPool = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val code = QCodeEntity.codeEntity

        return from(metricPool)
            .select(
                Projections.constructor(
                    MetricAnnualDto::class.java,
                    metricPool.metricId,
                    code.codeName.`as`("metricGroupName"),
                    metricPool.metricName,
                    metricPool.calculationType,
                    metricYear.zqlString,
                    metricPool.metricType,
                    metricYear.minValue,
                    metricYear.maxValue,
                    metricYear.weightValue,
                    Expressions.asNumber(0.0f),
                    metricYear.owner,
                    metricYear.comment
                )
            )
            .join(metricYear).on(metricPool.metricId.eq(metricYear.metric.metricId))
            .leftJoin(code).on(metricPool.metricGroup.eq(code.code))
            .where(super.likeIgnoreCase(metricYear.metricYear, year))
            .orderBy(metricYear.createDt.desc())
            .fetch()
    }
}
