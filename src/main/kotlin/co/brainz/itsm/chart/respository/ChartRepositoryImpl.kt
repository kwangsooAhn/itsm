package co.brainz.itsm.chart.respository

import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.entity.QChartEntity
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class ChartRepositoryImpl : QuerydslRepositorySupport(ChartEntity::class.java), ChartRepositoryCustom {
    override fun findChartList(searchGroupName: String, offset: Long?): List<ChartListDto> {
        val chart = QChartEntity.chartEntity
        val query = from(chart)
            .select(
                Projections.constructor(
                    ChartListDto::class.java,
                    chart.chartId,
                    chart.chartType,
                    chart.chartName,
                    chart.chartDesc,
                    chart.createUser.userName,
                    chart.createDt,
                    Expressions.numberPath(Long::class.java, "0")
                )
            )
            .where(
                super.like(chart.chartType, searchGroupName)
            ).orderBy(chart.chartName.asc())
        if (offset != null) {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT)
                .offset(offset)
        }
        val result = query.fetchResults()
        val chartList = mutableListOf<ChartListDto>()
        for (data in result.results) {
            data.totalCount = result.total
            chartList.add(data)
        }
        return chartList.toList()
    }
}
