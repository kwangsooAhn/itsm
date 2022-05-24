/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricManual.dto.MetricManualDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import co.brainz.itsm.sla.metricManual.entity.QMetricManualEntity
import co.brainz.itsm.sla.metricPool.dto.MetricSelectBoxDto
import co.brainz.itsm.sla.metricPool.entity.QMetricPoolEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MetricManualRepositoryImpl : QuerydslRepositorySupport(MetricManualEntity::class.java),
    MetricManualRepositoryCustom {
    val manual: QMetricManualEntity = QMetricManualEntity.metricManualEntity
    val metric: QMetricPoolEntity = QMetricPoolEntity.metricPoolEntity
    val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity
    val code: QCodeEntity = QCodeEntity.codeEntity

    override fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): PagingReturnDto {
        return PagingReturnDto(
            dataList = this.getMetricManualList(manualSearchCondition).fetch(),
            totalCount = this.getMetricManualCount(manualSearchCondition).fetchOne()
        )
    }

    private fun getMetricManualList(manualSearchCondition: MetricManualSearchCondition): JPQLQuery<MetricManualDto> {
        val query = from(manual)
            .select(
                Projections.constructor(
                    MetricManualDto::class.java,
                    manual.metricManualId,
                    metric.metricName,
                    manual.referenceDate,
                    manual.metricValue,
                    code.code,
                    manual.createDt,
                    user.userName
                )
            )
            .leftJoin(metric).on(metric.metricId.eq(manual.metric.metricId))
            .leftJoin(user).on(manual.userKey.eq(user.userKey))
            .leftJoin(code).on(metric.metricUnit.eq(code.code))
            .where(this.searchByBuilder(manualSearchCondition))

        query.limit(manualSearchCondition.contentNumPerPage)
        query.offset((manualSearchCondition.pageNum - 1) * manualSearchCondition.contentNumPerPage)
        return query
    }

    private fun getMetricManualCount(manualSearchCondition: MetricManualSearchCondition): JPQLQuery<Long> {
        return from(manual)
            .select(manual.count())
            .where(this.searchByBuilder(manualSearchCondition))
    }

    private fun searchByBuilder(manualSearchCondition: MetricManualSearchCondition): BooleanBuilder {
        val builder = BooleanBuilder()
        if (!manualSearchCondition.metricId.isNullOrEmpty()) {
            builder.and(manual.metric.metricId.eq(manualSearchCondition.metricId))
        }
        builder.and(manual.referenceDate.goe(manualSearchCondition.fromDt))
        builder.and(manual.referenceDate.lt(manualSearchCondition.toDt))
        return builder
    }

    override fun findMetricByMetricType(metricType: String): List<MetricSelectBoxDto> {
        return from(metric)
            .select(Projections.constructor(
                MetricSelectBoxDto::class.java,
                metric.metricId,
                metric.metricName
            ))
            .where(metric.metricType.eq(metricType))
            .fetch()
    }
}
