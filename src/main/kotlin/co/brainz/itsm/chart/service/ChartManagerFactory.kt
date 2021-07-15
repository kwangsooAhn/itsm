/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.service.impl.ActivityGauge
import co.brainz.itsm.chart.service.impl.BasicLine
import co.brainz.itsm.chart.service.impl.LineAndColumn
import co.brainz.itsm.chart.service.impl.Pie
import co.brainz.itsm.chart.service.impl.StackedBar
import co.brainz.itsm.chart.service.impl.StackedColumn
import org.springframework.stereotype.Component

@Component
class ChartManagerFactory(
    private val chartManagerService: ChartManagerService
) {

    fun getChartManager(chartType: String): ChartManager {
        return when (chartType) {
            ChartConstants.Type.BASIC_LINE.code -> BasicLine(chartManagerService)
            ChartConstants.Type.PIE.code -> Pie(chartManagerService)
            ChartConstants.Type.STACKED_COLUMN.code -> StackedColumn(chartManagerService)
            ChartConstants.Type.STACKED_BAR.code -> StackedBar(chartManagerService)
            ChartConstants.Type.LINE_AND_COLUMN.code -> LineAndColumn(chartManagerService)
            ChartConstants.Type.ACTIVITY_GAUGE.code -> ActivityGauge(chartManagerService)
            else -> throw AliceException(AliceErrorConstants.ERR, "ChartManager not found.")
        }
    }
}
