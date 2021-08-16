/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.dto.ChartListReturnDto
import co.brainz.itsm.chart.dto.ChartSearchCondition
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.entity.QChartEntity
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ChartRepositoryImpl : QuerydslRepositorySupport(ChartEntity::class.java), ChartRepositoryCustom {
    override fun findChartList(chartSearchCondition: ChartSearchCondition): ChartListReturnDto {
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
                super.like(chart.chartType, chartSearchCondition.searchGroupName)
            ).orderBy(chart.chartName.asc())
            .limit(chartSearchCondition.contentNumPerPage)
            .offset((chartSearchCondition.pageNum - 1) * chartSearchCondition.contentNumPerPage)
            .fetchResults()

        return ChartListReturnDto(
            data = query.results,
            paging = AlicePagingData(
                totalCount = query.total,
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }
}
