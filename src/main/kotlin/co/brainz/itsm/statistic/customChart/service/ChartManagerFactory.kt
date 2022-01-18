/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.service.impl.BasicLine
import co.brainz.itsm.statistic.customChart.service.impl.Pie
import co.brainz.itsm.statistic.customChart.service.impl.StackedBar
import co.brainz.itsm.statistic.customChart.service.impl.StackedColumn
import org.springframework.stereotype.Component

@Component
class ChartManagerFactory(
    private val chartManagerService: ChartManagerService
) {

    fun getChartManager(chartType: String): ChartManager {
        return when (chartType) {
            ChartConstants.Type.BASIC_LINE.code -> BasicLine(
                chartManagerService
            )
            ChartConstants.Type.PIE.code -> Pie(
                chartManagerService
            )
            ChartConstants.Type.STACKED_COLUMN.code -> StackedColumn(
                chartManagerService
            )
            ChartConstants.Type.STACKED_BAR.code -> StackedBar(
                chartManagerService
            )
            else -> throw AliceException(AliceErrorConstants.ERR, "ChartManager not found.")
        }
    }
}
