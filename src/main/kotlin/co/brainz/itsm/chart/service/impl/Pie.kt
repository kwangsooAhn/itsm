/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service.impl

import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartData
import co.brainz.itsm.chart.dto.ChartTagInstanceDto
import co.brainz.itsm.chart.service.ChartManager
import co.brainz.itsm.chart.service.ChartManagerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class Pie(
    chartManagerService: ChartManagerService
) : ChartManager(
    chartManagerService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun average(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstance: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        return valueList
    }

    override fun percent(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstance: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        return valueList
    }

    override fun count(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        return valueList
    }
}
