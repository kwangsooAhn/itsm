/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.respository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.statistic.customChart.dto.ChartDataDto
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition
import co.brainz.itsm.statistic.customChart.dto.CustomChartListDto
import com.querydsl.core.QueryResults

interface CustomChartRepositoryCustom : AliceRepositoryCustom {
    fun findChartList(chartSearchCondition: ChartSearchCondition): QueryResults<CustomChartListDto>
    fun findChartDataByChartIdsTemplateId(chartIds: Set<String>, templateId: String): List<ChartDataDto>
    fun existsDuplicationData(chartDto: ChartDto): Boolean
}
