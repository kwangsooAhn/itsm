/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service.impl

import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.service.ChartManager
import co.brainz.itsm.chart.service.ChartManagerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Pie(
    chartManagerService: ChartManagerService
) : ChartManager(chartManagerService) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun setChartConfigDetail(chartDto: ChartDto): LinkedHashMap<String, Any?> {
        return LinkedHashMap()
    }

    override fun setChartDetail(chartDto: ChartDto): ChartDto {
        return chartDto
    }
}
