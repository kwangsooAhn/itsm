/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.respository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.querydsl.QuerydslConstants
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartDataDto
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition
import co.brainz.itsm.statistic.customChart.dto.CustomChartListDto
import co.brainz.itsm.statistic.customChart.entity.ChartEntity
import co.brainz.itsm.statistic.customChart.entity.QChartEntity
import co.brainz.itsm.statistic.customReportTemplate.entity.QCustomReportTemplateMapEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomChartRepositoryImpl : QuerydslRepositorySupport(ChartEntity::class.java),
    CustomChartRepositoryCustom {
    override fun findChartList(chartSearchCondition: ChartSearchCondition): PagingReturnDto {
        val chart = QChartEntity.chartEntity
        val user = QAliceUserEntity.aliceUserEntity

        val query = from(chart)
            .select(
                Projections.constructor(
                    CustomChartListDto::class.java,
                    chart.chartId,
                    chart.chartType,
                    chart.chartName,
                    chart.chartDesc,
                    chart.createUser.userName,
                    chart.createDt
                )
            )
            .innerJoin(chart.createUser, user)
            .where(builder(chartSearchCondition, chart))

        if (chartSearchCondition.orderColName.isNullOrEmpty()) {
            query.orderBy(chart.createDt.desc(), chart.chartName.asc(), chart.chartType.asc())
        } else {
            val column = when (chartSearchCondition.orderColName) {
                ChartConstants.OrderColumn.CHART_NAME.code -> chart.chartName
                ChartConstants.OrderColumn.CHART_TYPE.code -> chart.chartType
                ChartConstants.OrderColumn.CHART_DESC.code -> chart.chartDesc
                ChartConstants.OrderColumn.CREATE_USER_NAME.code -> chart.createUser.userName
                else -> Expressions.stringPath(chart, ChartConstants.OrderColumn.CREATE_DT.code)
            }
            val direction = when (chartSearchCondition.orderDir) {
                QuerydslConstants.OrderSpecifier.DESC.code -> Order.DESC
                else -> Order.ASC
            }
            query.orderBy(OrderSpecifier(direction, column))
        }
        if (chartSearchCondition.isPaging) {
            query.limit(chartSearchCondition.contentNumPerPage)
            query.offset((chartSearchCondition.pageNum - 1) * chartSearchCondition.contentNumPerPage)
        }

        val countQuery = from(chart)
            .select(chart.count())
            .where(builder(chartSearchCondition, chart))
        return PagingReturnDto(
            dataList = query.fetch(),
            totalCount = countQuery.fetchOne()

        )
    }

    private fun builder(chartSearchCondition: ChartSearchCondition, chart: QChartEntity): BooleanBuilder {
        val builder = BooleanBuilder()
        builder.and(
            super.eq(chart.chartType, chartSearchCondition.searchGroupName)
        )
        builder.and(
            super.likeIgnoreCase(chart.chartName, chartSearchCondition.searchValue)
        )
        return builder
    }

    override fun findChartDataByChartIdsTemplateId(chartIds: Set<String>, templateId: String): List<ChartDataDto> {
        val chart = QChartEntity.chartEntity
        val reportMap = QCustomReportTemplateMapEntity.customReportTemplateMapEntity
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
            .where(
                chart.chartId.`in`(chartIds)
                    .and(reportMap.template.templateId.eq(templateId))
            )
            .orderBy(reportMap.displayOrder.asc())
            .fetch()
    }

    override fun existsDuplicationData(chartDto: ChartDto): Boolean {
        val chart = QChartEntity.chartEntity
        val query = from(chart)
            .where(
                chart.chartName.eq(chartDto.chartName)
                    .and(chart.chartType.eq(chartDto.chartType))
            )
        if (chartDto.chartId.isNotEmpty()) {
            query.where(!chart.chartId.eq(chartDto.chartId))
        }
        return query.fetch().size > 0
    }
}
