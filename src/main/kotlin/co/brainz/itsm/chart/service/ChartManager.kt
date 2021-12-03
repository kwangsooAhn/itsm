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
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartData
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartRange
import co.brainz.itsm.chart.dto.ChartTagInstanceDto
import co.brainz.itsm.chart.dto.average.ChartTagTokenData
import co.brainz.itsm.chart.dto.percent.ChartCategoryTag
import co.brainz.itsm.chart.dto.percent.ChartTagCount
import co.brainz.workflow.component.constants.WfComponentConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

abstract class ChartManager(
    private val chartManagerService: ChartManagerService
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Chart 조회
     * 1. 데이터를 Highchart 에서 사용할 수 있는 구조로 변경하여 전달
     *   1) category 목록 작성
     *     - PeriodUnit 값에 따라 년, 월, 일, 시 의 전체 목록 생성
     *   2) range(from, to) 범위에 해당되는 instance 목록 조회
     *   3) category 목록에 해당되는 instance 목록 적용
     *     - tag 별로 instanceEndDt 값이 instance 적용
     *     - count, percent, average 에 따른 처리
     */
    fun getChart(chartDto: ChartDto): ChartDto {
        // 1. [chartDto.chartConfig.range] to, from 값을 기준으로 category 를 생성
        val category = this.getCategory(chartDto.chartConfig)

        // 2. tag 별로 range(from, to) 범위의 instance 목록 가져오기
        val tagInstances = this.getTagInstances(chartDto.tags, chartDto.chartConfig.range)

        // 3. [chartDto.chartData] 변수에 처리한 데이터 적용
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

    abstract fun average(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstance: List<ChartTagInstanceDto>
    ): List<ChartData>

    abstract fun percent(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstance: List<ChartTagInstanceDto>
    ): List<ChartData>

    abstract fun count(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstances: List<ChartTagInstanceDto>
    ): List<ChartData>

    /**
     * Category 날짜와 조회한 instance 날짜를 비교하기 위해 날짜를 변환하는 함수
     */
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

    /**
     * (공통) 평균 계산
     *  1. 소수점 2자리까지 저장
     */
    fun valueOfAverage(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstances: List<ChartTagInstanceDto>
    ): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        // 평균 값에 사용되는 컴포넌트는 아래의 타입으로 고정된다.
        val componentTypeSet = setOf(
            WfComponentConstants.ComponentTypeCode.SELECT.code,
            WfComponentConstants.ComponentTypeCode.RADIO.code,
            WfComponentConstants.ComponentTypeCode.CHECKBOX.code
        )

        val instanceTagTokenDataList = mutableListOf<ChartTagTokenData>()
        tagInstances.forEach { tagInstance ->
            // tagValue 로 연결된 컴포넌트 tag 정보를 찾는다.
            // tagInstance.tag 는 chart 의 tag 정보이므로 tagValue 로 실제 사용중인 component Tag 목록을 조회한다.
            val componentTagList = chartManagerService.getTagValueList(
                AliceTagConstants.TagType.COMPONENT.code,
                listOf(tagInstance.tag.tagValue)
            )
            val componentIds = mutableSetOf<String>()
            componentTagList.forEach {
                componentIds.add(it.targetId)
            }

            // tag 별 instance 목록를 합쳐 모든 instance 의 마지막 token 를 조회한 후 tokenId 를 추출한다.
            val instanceIds = mutableSetOf<String>()
            tagInstance.instances.forEach {
                instanceIds.add(it.instanceId)
            }
            val lastTokenList = chartManagerService.getLastTokenList(instanceIds)
            val tokenIds = mutableSetOf<String>()
            lastTokenList.forEach {
                tokenIds.add(it.tokenId)
            }

            // componentIds, tokenIds, componentTypeSet 으로 실제 저장된 값을 조회한다. (instance 의 기본 정보도 같이 조회한다.)
            // instance 정보는 category 별 분리시 사용할 정보이다. (instanceEndDt)
            val instanceTokenDataList = chartManagerService.getTokenDataList(componentIds, tokenIds, componentTypeSet)

            // Tag 별로 데이터를 저장
            instanceTagTokenDataList.add(
                ChartTagTokenData(
                    tag = tagInstance.tag,
                    tokenDataList = instanceTokenDataList
                )
            )
        }

        // category 별 데이터 셋팅
        category.iterator().forEach {
            val categoryDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val periodUnitValue = this.getPeriodUnitValue(chartConfig.periodUnit!!, categoryDateTime)

            // Tag 별로 loop 진행하면서 숫자인 값을 합치고, 전체 건수로 나눈다.
            instanceTagTokenDataList.forEach { instanceTagTokenData ->
                var totalCount = 0
                var valueSum = 0.0
                instanceTagTokenData.tokenDataList.forEach { tokenData ->
                    if (periodUnitValue == this.getPeriodUnitValue(chartConfig.periodUnit!!, tokenData.instanceEndDt)) {
                        totalCount++ // 숫자가 아닌 잘못된 값도 전체 건수에 포함한다. (제외하려면 한줄 아래로...)
                        if (tokenData.value.chars().allMatch(Character::isDigit) && tokenData.value.isNotEmpty()) {
                            valueSum += tokenData.value.toDouble()
                        }
                    }
                }

                val avgValue = if (totalCount > 0) valueSum / totalCount else 0.0
                valueList.add(
                    ChartData(
                        id = instanceTagTokenData.tag.targetId,
                        category = it,
                        value = String.format("%.2f", avgValue),
                        series = instanceTagTokenData.tag.tagValue
                    )
                )
            }
        }

        return valueList
    }

    /**
     * (공통) 퍼센트 계산
     *  1. 소수점 2자리까지 저장
     */
    fun valueOfPercent(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstances: List<ChartTagInstanceDto>
    ): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        // category 별로 tag 정보와 건수를 담는다. (데이터는 category x tag 수)
        val categoryTagList = mutableListOf<ChartCategoryTag>()
        category.iterator().forEach {
            val categoryDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            val periodUnitValue = this.getPeriodUnitValue(chartConfig.periodUnit!!, categoryDateTime)

            var totalCount = 0 // category 별 전체 건수 (tag 별 퍼센트 비율이기 때문에 category 별로 묶는다)
            val tagCountList = mutableListOf<ChartTagCount>()
            // tag 수만큼 반복 처리
            tagInstances.forEach { tagInstances ->
                var count = 0
                tagInstances.instances.forEach { instance ->
                    if (periodUnitValue == this.getPeriodUnitValue(chartConfig.periodUnit!!, instance.instanceEndDt!!)) {
                        count++
                    }
                }
                // 각 tag 별 건수 저장 (tag 별 1건씩 저장)
                tagCountList.add(
                    ChartTagCount(
                        tag = tagInstances.tag,
                        count = count
                    )
                )
                totalCount += count
            }
            // category 데이터 저장 (tag 에 상관없이 전체 건수, tag 별 데이터)
            categoryTagList.add(
                ChartCategoryTag(
                    category = it,
                    totalCount = totalCount,
                    tagCountList = tagCountList
                )
            )
        }

        // category 별 전체 건수 와 tag 건수를 비교하여 percent 구하기
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

    /**
     * (공통) 카운트
     */
    fun valueOfCount(
        chartConfig: ChartConfig,
        category: LinkedHashSet<String>,
        tagInstances: List<ChartTagInstanceDto>
    ): List<ChartData> {
        val valueList = mutableListOf<ChartData>()

        category.iterator().forEach {
            // category 값을 periodUnit 에 따라 비교할 수 있는 값으로 변경한다.
            val categoryDateTime = LocalDateTime.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
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
                        category = it,
                        series = tagInstance.tag.tagValue,
                        value = count.toString()
                    )
                )
            }
        }

        return valueList
    }

    /**
     * Range (from, to) 값으로 category 목록 작성
     */
    private fun getCategory(chartConfig: ChartConfig): LinkedHashSet<String> {
        val category = LinkedHashSet<String>()

        val from = chartConfig.range.from!!
        val to = chartConfig.range.to!!
        when (chartConfig.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                val period: Long = ChronoUnit.YEARS.between(from, to)
                for (i in 0..period) {
                    category.add(
                        ((chartConfig.range.from!!.year + i).toString()) + "-01-01 00:00:00"
                    )
                }
            }
            ChartConstants.Unit.MONTH.code -> {
                val period: Long = ChronoUnit.MONTHS.between(from, to)
                for (i in 0..period) {
                    val nextCategory = from.plusMonths(i)
                    category.add(
                        (nextCategory.year.toString() + "-" + String.format("%02d", nextCategory.monthValue)) + "-01 00:00:00"
                    )
                }
            }
            ChartConstants.Unit.DAY.code -> {
                val period: Long = ChronoUnit.DAYS.between(from, to)
                for (i in 0..period) {
                    val nextCategory = from.plusDays(i)
                    category.add(
                        (nextCategory.year.toString() + "-" + String.format("%02d", nextCategory.monthValue) + "-" + String.format("%02d", nextCategory.dayOfMonth)) + " 00:00:00"
                    )
                }
            }
            ChartConstants.Unit.HOUR.code -> {
                val period: Long = ChronoUnit.HOURS.between(from, to)
                for (i in 0..period) {
                    val nextCategory = from.plusHours(i)
                    category.add(
                        (nextCategory.year.toString() + "-" + String.format("%02d", nextCategory.monthValue) + "-" + String.format("%02d", nextCategory.dayOfMonth) + " " + String.format("%02d", nextCategory.hour)) + ":00:00"
                    )
                }
            }
        }

        return category
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
