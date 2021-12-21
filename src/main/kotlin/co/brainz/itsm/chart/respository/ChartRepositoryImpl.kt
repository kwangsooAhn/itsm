/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.framework.auth.entity.*
import co.brainz.itsm.chart.dto.*
import co.brainz.itsm.chart.entity.*
import co.brainz.itsm.report.entity.*
import com.querydsl.core.*
import com.querydsl.core.types.*
import org.springframework.data.jpa.repository.support.*
import org.springframework.stereotype.*

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
                .orderBy(chart.chartName.asc())
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
                        // template id 비교 구문
                        .and(reportMap.template.templateId.eq(templateId))
                )
                .orderBy(reportMap.displayOrder.asc())
                .fetch()
    }
}
