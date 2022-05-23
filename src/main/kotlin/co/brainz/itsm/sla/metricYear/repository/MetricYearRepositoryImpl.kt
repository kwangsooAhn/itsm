/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricPoolEntity
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearDataDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearExcelDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.QMetricYearEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
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
            .where(this.searchByBuilder(metricYearSearchCondition, metricYear))
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
            .where(this.searchByBuilder(metricYearSearchCondition, metricYear))
    }

    private fun searchByBuilder(metricYearSearchCondition: MetricYearSearchCondition, metricYear: QMetricYearEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.likeIgnoreCase(metricYear.metricYear, metricYearSearchCondition.year)
        )
        return builder
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

    override fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        val metric = QMetricPoolEntity.metricPoolEntity
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
            .where(metricYear.metricYear.`in`(metricLoadCondition.source))

        if (!metricLoadCondition.target.isNullOrEmpty()) {
            query.where(metricYear.metricYear.notIn(metricLoadCondition.target))
        }
        if (!metricLoadCondition.type.isNullOrEmpty()) {
            query.where(metric.metricType.`in`(metricLoadCondition.type))
        }

        return query.fetch()
    }

    override fun findMetricYearListForExcel(metricYearSearchCondition: MetricYearSearchCondition): List<MetricYearExcelDto> {
        val metric = QMetricPoolEntity.metricPoolEntity
        val metricYear = QMetricYearEntity.metricYearEntity
        val groupCode = QCodeEntity.codeEntity

        //TODO 결과값 출력 변경 해야함
        return from(metricYear)
            .select(
                Projections.constructor(
                    MetricYearExcelDto::class.java,
                    groupCode.codeName,
                    metric.metricName,
                    metricYear.minValue,
                    metricYear.maxValue,
                    metricYear.weightValue,
                    Expressions.asNumber(0L),
                    metricYear.owner,
                    metricYear.comment
                )
            )
            .leftJoin(metric).on(metric.eq(metricYear.metric))
            .leftJoin(groupCode).on(metric.metricGroup.eq(groupCode.code))
            .where(metricYear.metricYear.`in`(metricYearSearchCondition.year))
            .fetch()
    }
}
