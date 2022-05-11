package co.brainz.itsm.sla.metricManual.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.sla.metricManual.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricManual.dto.MetricLoadDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualKeyDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.entity.MetricManualEntity
import co.brainz.itsm.sla.metricManual.entity.QMetricManualEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricEntity
import co.brainz.itsm.sla.metricPool.entity.QMetricGroupEntity
import co.brainz.itsm.sla.metricYear.entity.QMetricYearEntity
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

    override fun existsByMetricIdAndReferenceDt(metricManualKeyDto: MetricManualKeyDto): Boolean {
        val manual = QMetricManualEntity.metricManualEntity
        return from(manual)
            .select(manual.count())
            .where(
                manual.referenceDt.eq(metricManualKeyDto.referenceDt),
                manual.metric.metricId.eq(metricManualKeyDto.metricId)
            )
            .fetchOne() > 0
    }
}
