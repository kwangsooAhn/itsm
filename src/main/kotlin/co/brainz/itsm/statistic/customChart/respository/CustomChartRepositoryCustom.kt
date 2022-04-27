/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.respository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customChart.dto.ChartDataDto
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition

interface CustomChartRepositoryCustom : AliceRepositoryCustom {
    fun findChartList(chartSearchCondition: ChartSearchCondition): PagingReturnDto
    fun findChartDataByChartIdsTemplateId(chartIds: Set<String>, templateId: String): List<ChartDataDto>
    fun existsDuplicationData(chartDto: ChartDto): Boolean
}
