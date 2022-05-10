package co.brainz.itsm.sla.metricManual.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricManual.dto.MetricManualDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import co.brainz.itsm.sla.metricManual.entity.QMetricManualEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MetricManualRepositoryImpl : QuerydslRepositorySupport(MetricManualEntity::class.java),
    MetricManualRepositoryCustom {

    override fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): PagingReturnDto {
        val manual = QMetricManualEntity.metricManualEntity
        val metric = QMetricEntity.metricEntity
        val user = QAliceUserEntity.aliceUserEntity
        val code = QCodeEntity.codeEntity
        val query = from(manual)
            .select(
                Projections.constructor(
                    MetricManualDto::class.java,
                    metric.metricId,
                    metric.metricName,
                    manual.referenceDt,
                    manual.metricValue,
                    code.codeName,
                    manual.createDt,
                    user.userName
                )
            )
            .leftJoin(metric).on(metric.metricId.eq(manual.metric.metricId))
            .leftJoin(user).on(manual.userKey.eq(user.userKey))
            .leftJoin(code).on(metric.metricUnit.eq(code.code))
            .where(this.searchByBuilder(manualSearchCondition, manual, metric))

        query.limit(manualSearchCondition.contentNumPerPage)
        query.offset((manualSearchCondition.pageNum - 1) * manualSearchCondition.contentNumPerPage)

        val count = from(manual)
            .select(manual.count())
            .where(this.searchByBuilder(manualSearchCondition, manual, metric))

        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = count.fetchCount()
        )

    }

    private fun searchByBuilder(
        manualSearchCondition: MetricManualSearchCondition,
        manual: QMetricManualEntity,
        metric: QMetricEntity
    ): BooleanBuilder {
        val builder = BooleanBuilder()
        if (manualSearchCondition.searchValue!!.isNotEmpty()) {
            builder.and(metric.metricName.`in`(manualSearchCondition.searchValue))
        }
        builder.and(manual.createDt.goe(manualSearchCondition.fromDt))
        builder.and(manual.createDt.lt(manualSearchCondition.toDt))
        return builder
    }
}
