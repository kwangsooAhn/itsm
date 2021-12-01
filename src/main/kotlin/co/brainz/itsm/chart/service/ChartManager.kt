/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartCategoryTag
import co.brainz.itsm.chart.dto.ChartCategoryTagInstance
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartData
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartRange
import co.brainz.itsm.chart.dto.ChartTagCount
import co.brainz.itsm.chart.dto.ChartTagInstanceDto
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.instance.entity.WfInstanceEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

abstract class ChartManager(
    private val chartManagerService: ChartManagerService
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getChart(chartDto: ChartDto): ChartDto {
        
        // 1. 기본 셋팅 정보는 다 되어 있고
        // 2. 데이터를 조회한다.
        // 3-1. 문서 목록 조회

        // range 로 from, to 구하기
        // TODO: range 범위 정하기 (FrontEnd or BackEnd)
        // 2021-11-30 화면에서 from, to를 작성해서 보내주기로 함
        //chartDto.chartConfig.range = this.getRange(chartDto.chartConfig)

        // TODO: range 범위에 간격 값을 기준으로 category 를 생성
        val category = this.getCategory(chartDto.chartConfig)

        // TODO: tag 별 range 안의 instance 목록을 가져오기
        val tagInstances = this.getTagInstances(chartDto.tags, chartDto.chartConfig.range)

        // TODO: 가져온 데이터로 설정에 따라 데이터를 가공하여 chartDto.data 를 만든다.
        val data = when (chartDto.chartConfig.operation) {
            ChartConstants.Operation.COUNT.code -> this.count(chartDto.chartConfig, category, tagInstances)
            ChartConstants.Operation.PERCENT.code -> this.percent(chartDto.chartConfig, category, tagInstances)
            ChartConstants.Operation.AVERAGE.code -> this.average(chartDto.chartConfig, category, tagInstances)
            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Chart Operation Error]"
            )
        }
        chartDto.chartData.addAll(data)

        return chartDto
    }

    abstract fun average(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstance: List<ChartTagInstanceDto>): List<ChartData>

    abstract fun percent(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstance: List<ChartTagInstanceDto>): List<ChartData>

    abstract fun count(chartConfig: ChartConfig, category: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData>

    private fun getPeriodUnitValue(periodUnit: String, dateTime: LocalDateTime): String {
        val year = dateTime.year.toString()
        val month = String.format("%02d", dateTime.monthValue)
        val day = String.format("%02d", dateTime.dayOfMonth)
        val hour = String.format("%02d", dateTime.hour)
        return when (periodUnit) {
            ChartConstants.Unit.YEAR.code -> year
            ChartConstants.Unit.MONTH.code -> "$year-$month"
            ChartConstants.Unit.DAY.code -> "$year-$month-$day"
            ChartConstants.Unit.HOUR.code -> "$year-$month-$day $hour"
            else -> throw AliceException(
                AliceErrorConstants.ERR_00005,
                AliceErrorConstants.ERR_00005.message + "[Chart periodUnitValue Error]"
            )
        }
    }

    fun valueOfAverage(chartConfig: ChartConfig, categories: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        // tag 정보로 component type 의 차트를 조회해 온다.
        val tagValues = mutableSetOf<String>()
        tagInstances.forEach { tagInstance ->
            tagValues.add(tagInstance.tag.tagValue)
        }
        val componentTagList = chartManagerService.getTagValueList(AliceTagConstants.TagType.COMPONENT.code, tagValues.toList())




        return valueList
    }


    fun valueOfAverage2(chartConfig: ChartConfig, categories: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        // category 별로 instance 목록을 붙인다.
        val categoryTagInstances = mutableListOf<ChartCategoryTagInstance>()
        categories.iterator().forEach { category ->
            val categoryDateTime = LocalDateTime.parse(category, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val periodUnitValue = this.getPeriodUnitValue(chartConfig.periodUnit!!, categoryDateTime)

            tagInstances.forEach { tagInstances ->
                val instanceList = mutableListOf<WfInstanceEntity>()
                tagInstances.instances.forEach { instance ->
                    if (periodUnitValue == this.getPeriodUnitValue(chartConfig.periodUnit!!, instance.instanceEndDt!!)) {
                        instanceList.add(instance)
                    }
                }

                categoryTagInstances.add(
                    ChartCategoryTagInstance(
                        category = category,
                        tag = tagInstances.tag,
                        instances = instanceList
                    )
                )
            }
        }

        // category 별로 tag, instance를 구한 걸로
        // tag 정보는 chart에 설정한 tag 정보임으로... target_id가 component_id가 될 수 없다. (여기서 tag는 chart에서 설정한 tag를 의미한다.)
        // tagValue 를 모아서 component 타입의 tag 정보를 조회힌다. (원래는 1개여야 하지만.. 복수가 될 수 있따.)
        val tagValues = mutableSetOf<String>()
        categoryTagInstances.forEach {
            tagValues.add(it.tag.tagValue)
        }


        // category 별 instance 목록으로 작업
        val componentTypeSet = setOf(
            WfComponentConstants.ComponentTypeCode.SELECT.code,
            WfComponentConstants.ComponentTypeCode.RADIO.code,
            WfComponentConstants.ComponentTypeCode.CHECKBOX.code
        )

        //category, component_id, token_id 값을 전부 담아서.. 한번에 데이터를 조회한 후.. 조건을 줘서 category 데이터를 만든다.
        /*val categoryTokenDataSearchList = mutableListOf<ChartCategoryTokenDataSearchDto>()

        categoryTagInstances.forEach { categoryTagInstance ->
            val category = categoryTagInstance.category
            val componentId = categoryTagInstance.tag.targetId
            categoryTagInstance.instances.forEach { instance ->
                var lastTokenId = ""
                //run loop
                instance.tokens?.forEach { token ->
                    if (token.element.elementType == WfElementConstants.ElementType.COMMON_END_EVENT.value) {
                        lastTokenId = token.tokenId
                    }
                }
                // componentId와 lastTokenId로 실제 데이터를 조회한다.
                categoryTokenDataSearchList.add(
                    ChartCategoryTokenDataSearchDto(
                        category = category,
                        componentId = componentId,
                        tokenId = lastTokenId
                    )
                )

                // 조회된 데이터를 category의 각 tag별로 저장한다.
            }
        }*/

        // categoryTokenDataSearchList 로 wf_token_data 에서 데이터를 조회한다.
        // 조회 후 3개의 key로 각각의 데이터를 분배한다.

        /*categoryTokenDataSearchList.forEach {
            it.
        }*/
        //chartManagerService.getCategoryTokenDataList(categoryTokenDataSearchList)


        return valueList
    }

    fun valueOfPercent(chartConfig: ChartConfig, categories: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()
        // category 별로 태그정보와 건수를 담는다.
        val categoryTagList = mutableListOf<ChartCategoryTag>()
        categories.iterator().forEach { category ->
            val categoryDateTime = LocalDateTime.parse(category, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val periodUnitValue = this.getPeriodUnitValue(chartConfig.periodUnit!!, categoryDateTime)

            var totalCount = 0
            val tagCountList = mutableListOf<ChartTagCount>()
            tagInstances.forEach { tagInstances ->
                var count = 0
                tagInstances.instances.forEach { instance ->
                    if (periodUnitValue == this.getPeriodUnitValue(chartConfig.periodUnit!!, instance.instanceEndDt!!)) {
                        count++
                    }
                }
                // 각 태그별 건수를 저장
                tagCountList.add(
                    ChartTagCount(
                        count = count,
                        tag = tagInstances.tag
                    )
                )
                // category 별 전체 건수
                totalCount += count
            }

            categoryTagList.add(
                ChartCategoryTag(
                    category = category,
                    totalCount = totalCount,
                    tagCountList = tagCountList
                )
            )
        }

        // category 별 전체 건수 와 tag 의 건수를 비교하여 percent 구하기
        categoryTagList.forEach { categoryTag ->
            categoryTag.tagCountList.forEach { tagCount ->
                var percentValue = 0.0
                if (categoryTag.totalCount > 0) {
                    percentValue = (tagCount.count.toDouble() / categoryTag.totalCount.toDouble()) * 100
                }
                valueList.add(
                    ChartData(
                        id = tagCount.tag.tagId.toString(),
                        category = categoryTag.category,
                        value = String.format("%.2f", percentValue),
                        series = tagCount.tag.tagValue
                    )
                )
            }
        }

        return valueList
    }

    fun valueOfCount(chartConfig: ChartConfig, categories: LinkedHashSet<String>, tagInstances: List<ChartTagInstanceDto>): List<ChartData> {
        val valueList = mutableListOf<ChartData>()
        categories.iterator().forEach { category ->
            // category 값을 periodUnit 에 따라 비교할 수 있는 값으로 변경한다.
            val categoryDateTime = LocalDateTime.parse(category, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val periodUnitValue = this.getPeriodUnitValue(chartConfig.periodUnit!!, categoryDateTime)
            var count = 0
            // tag 별 instance 를 loop 돌리면서 count 계산
            tagInstances.forEach { tagInstance ->
                tagInstance.instances.forEach { instance ->
                    if (periodUnitValue == this.getPeriodUnitValue(chartConfig.periodUnit!!, instance.instanceEndDt!!)) {
                        count++
                    }
                }
                valueList.add(
                    ChartData(
                        id = tagInstance.tag.tagId.toString(),
                        category = category,
                        series = tagInstance.tag.tagValue,
                        value = count.toString()
                    )
                )
            }
        }
        return valueList
    }

    private fun getCategory(chartConfig: ChartConfig): LinkedHashSet<String> {
        val category = LinkedHashSet<String>()

        val from = chartConfig.range.from!!
        val to = chartConfig.range.to!!
        when (chartConfig.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                val period: Long = ChronoUnit.YEARS.between(from, to)
                for (i in 0..period) {
                    category.add(((chartConfig.range.from!!.year + i).toString()) + "-01-01 00:00:00")
                }
            }
            ChartConstants.Unit.MONTH.code -> {
                val period: Long = ChronoUnit.MONTHS.between(from, to)
                for (i in 0..period) {
                    val nextCategory = from.plusMonths(i)
                    category.add((nextCategory.year.toString() + "-" + String.format("%02d", nextCategory.monthValue)) + "-01 00:00:00")
                }
            }
            ChartConstants.Unit.DAY.code -> {
                val period: Long = ChronoUnit.DAYS.between(from, to)
                for (i in 0..period) {
                    val nextCategory = from.plusDays(i)
                    category.add((nextCategory.year.toString() + "-" + String.format("%02d", nextCategory.monthValue) + "-" + String.format("%02d", nextCategory.dayOfMonth)) + " 00:00:00")
                }
            }
            ChartConstants.Unit.HOUR.code -> {
                val period: Long = ChronoUnit.HOURS.between(from, to)
                for (i in 0..period) {
                    val nextCategory = from.plusHours(i)
                    category.add((nextCategory.year.toString() + "-" + String.format("%02d", nextCategory.monthValue) + "-" + String.format("%02d", nextCategory.dayOfMonth) + " " + String.format("%02d", nextCategory.hour)) + ":00:00")
                }
            }
        }

        return category
    }

    private fun getRange(chartConfig: ChartConfig): ChartRange {
        val range = chartConfig.range.copy()
        when (chartConfig.range.type) {
            ChartConstants.Range.ALL.code -> {
                range.from = LocalDate.of(2000, 1, 1).atStartOfDay()
                range.to = LocalDateTime.now()
            }
            ChartConstants.Range.BETWEEN.code -> {
                range.to = chartConfig.range.to!!.plusDays(1).minusSeconds(1)
            }
            ChartConstants.Range.LAST_DAY.code -> {
                range.from = LocalDateTime.now().minusDays(1)
                range.to = LocalDateTime.now()
            }
            ChartConstants.Range.LAST_MONTH.code -> {
                val lastMonth = YearMonth.from(LocalDateTime.now().minusMonths(1))
                range.from = lastMonth.atDay(1).atStartOfDay()
                range.to = lastMonth.atEndOfMonth().plusDays(1).atStartOfDay().minusSeconds(1)
            }
        }

        return range
    }

    /**
     * [tags] 목록으로 각각의 tag 별 range 범위내 instance 목록을 조회
     */
    private fun getTagInstances(tags: List<AliceTagDto>, range: ChartRange): List<ChartTagInstanceDto> {
        val tagInstances = mutableListOf<ChartTagInstanceDto>()
        tags.forEach { tag ->
            tagInstances.add(
                ChartTagInstanceDto(
                    tag = tag,
                    instances = chartManagerService.getInstanceListInTag(tag.tagValue, range)
                )
            )
        }
        return tagInstances
    }
}
