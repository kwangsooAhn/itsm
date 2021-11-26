/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.chart.dto.ChartDataDto
import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.dto.ChartSearchCondition
import com.querydsl.core.QueryResults

interface ChartRepositoryCustom : AliceRepositoryCustom {
    fun findChartList(chartSearchCondition: ChartSearchCondition): QueryResults<ChartListDto>
    fun findChartDataByChartIds(chartIds: Set<String>): List<ChartDataDto>
}
