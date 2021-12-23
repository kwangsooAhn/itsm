/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.chart.dto.ChartDataDto
import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.dto.ChartSearchCondition
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.entity.QChartEntity
import co.brainz.itsm.report.entity.QReportTemplateMapEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ChartRepositoryImpl : QuerydslRepositorySupport(ChartEntity::class.java), ChartRepositoryCustom {
    override fun findChartList(chartSearchCondition: ChartSearchCondition): QueryResults<ChartListDto> {
        val chart = QChartEntity.chartEntity
        val user = QAliceUserEntity.aliceUserEntity
        val query = from(chart)
            .select(
                Projections.constructor(
                    ChartListDto::class.java,
                    chart.chartId,
                    chart.chartType,
                    chart.chartName,
                    chart.chartDesc,
                    chart.createUser.userName,
                    chart.createDt
                )
            )
            .innerJoin(chart.createUser, user)
            .where(
                super.eq(chart.chartType, chartSearchCondition.searchGroupName)
            )
            .where(
                super.likeIgnoreCase(chart.chartName, chartSearchCondition.searchValue)
            )
            .orderBy(chart.createDt.desc(), chart.chartName.asc(), chart.chartType.asc())
        if (chartSearchCondition.isPaging) {
            query.limit(chartSearchCondition.contentNumPerPage)
            query.offset((chartSearchCondition.pageNum - 1) * chartSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }

    override fun findChartDataByChartIdsTemplateId(chartIds: Set<String>, templateId: String): List<ChartDataDto> {
        val chart = QChartEntity.chartEntity
        val reportMap = QReportTemplateMapEntity.reportTemplateMapEntity
        return from(chart)
            .select(
                Projections.constructor(
                    ChartDataDto::class.java,
                    chart.chartId,
                    chart.chartType,
                    chart.chartName,
                    chart.chartDesc,
                    chart.chartConfig
                )
            )
            .leftJoin(reportMap).on(chart.chartId.eq(reportMap.chartId))
            .where(chart.chartId.`in`(chartIds)
                .and(reportMap.template.templateId.eq(templateId))
            )
            .orderBy(reportMap.displayOrder.asc())
            .fetch()
    }
}
