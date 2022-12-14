/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.service.impl

import co.brainz.framework.tag.dto.AliceTagDto
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
class Pie(
    chartManagerService: ChartManagerService,
    chartConditionService: ChartConditionService
) : ChartManager(
    chartManagerService, chartConditionService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun average(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstance: List<ChartTagInstanceDto>
    ): List<ChartData> {
        return emptyList() // Pie 차트는 평균 없음
    }

    override fun percent(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstance: List<ChartTagInstanceDto>
    ): List<ChartData> {
        return emptyList() // Pie 차트는 퍼센트 없음
    }

    override fun count(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstances: List<ChartTagInstanceDto>
    ): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        // category: tagValue, series: 빈 값
        val tagList = mutableListOf<AliceTagDto>()
        tagInstances.forEach { tagInstance ->
            tagList.add(tagInstance.tag)
        }

        tagList.forEach { tag ->
            var tagCount = 0
            tagInstances.forEach { tagInstance ->
                if (tagInstance.tag == tag) {
                    tagCount += tagInstance.instances.size
                }
            }

            valueList.add(
                ChartData(
                    id = tag.tagId.toString(),
                    category = tag.tagValue,
                    value = tagCount.toString(),
                    series = "" // 하이차트에서 파이차트는 시리즈는 1개이며 화면에서 카테고리를 이용해서 시리즈 데이터를 만들어서 사용함.
                )
            )
        }

        return valueList
    }
}
