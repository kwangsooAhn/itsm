/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.dto.ChartListReturnDto
import co.brainz.itsm.chart.dto.ChartSearchDto
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.entity.QChartEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ChartRepositoryImpl : QuerydslRepositorySupport(ChartEntity::class.java), ChartRepositoryCustom {
    override fun findChartList(chartSearchDto: ChartSearchDto): ChartListReturnDto {
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
                super.like(chart.chartType, chartSearchDto.search)
            ).orderBy(chart.chartName.asc())
        if (chartSearchDto.limit != null && chartSearchDto.limit > -1) {
            query.limit(chartSearchDto.limit)
        }
        if (chartSearchDto.offset != null && chartSearchDto.offset > -1) {
            query.offset(chartSearchDto.offset)
        }

        val result = query.fetchResults()
        return ChartListReturnDto(
            data = result.results,
            totalCount = result.total
        )
    }
}
