package co.brainz.itsm.chart.respository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.chart.dto.ChartListDto

interface ChartRepositoryCustom : AliceRepositoryCustom {
    fun findChartList(searchGroupName: String, offset: Long?): List<ChartListDto>
}
