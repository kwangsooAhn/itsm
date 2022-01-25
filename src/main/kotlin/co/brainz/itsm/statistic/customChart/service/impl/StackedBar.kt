/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service.impl

import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartData
import co.brainz.itsm.statistic.customChart.dto.ChartTagInstanceDto
import co.brainz.itsm.statistic.customChart.service.ChartConditionService
import co.brainz.itsm.statistic.customChart.service.ChartManager
import co.brainz.itsm.statistic.customChart.service.ChartManagerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class StackedBar(
    chartManagerService: ChartManagerService,
    chartConditionService: ChartConditionService
) : ChartManager(
    chartManagerService, chartConditionService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun average(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstance: List<ChartTagInstanceDto>): List<ChartData> {
        return super.valueOfAverage(chartConfig, category, tagInstance)
    }

    override fun percent(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstance: List<ChartTagInstanceDto>): List<ChartData> {
        return super.valueOfPercent(chartConfig, category, tagInstance)
    }

    override fun count(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData> {
        return super.valueOfCount(chartConfig, category, tagInstances)
    }
}
