/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.respository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.chart.dto.ChartListReturnDto
import co.brainz.itsm.chart.dto.ChartSearchCondition

interface ChartRepositoryCustom : AliceRepositoryCustom {
    fun findChartList(chartSearchCondition: ChartSearchCondition): ChartListReturnDto
}
