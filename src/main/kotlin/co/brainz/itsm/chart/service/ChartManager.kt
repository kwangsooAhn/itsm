/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartDateTimeDto
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit

abstract class ChartManager(
    private val chartManagerService: ChartManagerService
) {
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    lateinit var chartConfig: ChartConfig

    abstract fun setChartConfigDetail(chartDto: ChartDto): LinkedHashMap<String, Any?>

    fun getChart(chartDto: ChartDto): ChartDto {
        this.chartConfig = chartDto.chartConfig
        this.setChartDetail(chartDto)
        chartDto.propertyJson = this.getChartProperty(chartDto)
        return chartDto
    }

    abstract fun setChartDetail(chartDto: ChartDto): ChartDto

    /**
     * 차트 생성 관련 JsonArray 생성
     */
    private fun getChartProperty(chart: ChartDto): String {
        val instanceList = this.getInstanceListInTags(chart)
        val propertyList = mutableListOf<Map<String, Any>>()
        val durationDoc = this.getDurationDoc(chart, instanceList)
        val map = LinkedHashMap<String, Any>()
        map["title"] = chart.chartName
        map["operation"] = this.calculateOperation(durationDoc)
        propertyList.add(map)
        propertyList.add(durationDoc)

        return mapper.writeValueAsString(propertyList)
    }

    private fun getInstanceListInTags(chart: ChartDto): List<WfInstanceEntity> {
        val tags = mutableSetOf<String>()
        chart.chartConfig.tags?.forEach { tag ->
            tags.add(tag)
        }
        return chartManagerService.getInstanceListInTags(tags)
    }

    private fun getPeriodYear(
        chartDateTime: ChartDateTimeDto,
        instanceList: List<WfInstanceEntity>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (year in chartDateTime.startYear until chartDateTime.endDateTime!!.year + 1) {
            val docList = mutableListOf<HashMap<String, Any>>()
            instanceList.forEach { instance ->
                when (instance.instanceStartDt!!.year) {
                    year -> {
                        val docMap = HashMap<String, Any>()
                        docMap["instanceId"] = instance.instanceId
                        docMap["endDt"] = instance.instanceEndDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[year.toString()] = docList
        }
        return durationMap
    }

    private fun getPeriodMonth(
        instanceList: List<WfInstanceEntity>,
        dateFormatList: MutableList<String>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (dateFormat in dateFormatList) {
            val docList = mutableListOf<HashMap<String, Any>>()
            instanceList.forEach { instance ->
                val docYear = instance.instanceEndDt!!.year
                val docMonth = instance.instanceEndDt!!.monthValue
                when (docYear.toString() + this.addStringFormat(docMonth)) {
                    dateFormat -> {
                        val docMap = HashMap<String, Any>()
                        docMap["instanceId"] = instance.instanceId
                        docMap["endDt"] = instance.instanceEndDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[dateFormat] = docList
        }
        return durationMap
    }

    private fun getPeriodDate(
        instanceList: List<WfInstanceEntity>,
        dateFormatList: MutableList<String>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (dateFormat in dateFormatList) {
            val docList = mutableListOf<HashMap<String, Any>>()
            instanceList.forEach { instance ->
                val docYear = instance.instanceEndDt!!.year
                val docMonth = instance.instanceEndDt!!.monthValue
                val docDays = instance.instanceEndDt!!.dayOfMonth
                when (docYear.toString() + this.addStringFormat(docMonth) + this.addStringFormat(docDays)) {
                    dateFormat -> {
                        val docMap = HashMap<String, Any>()
                        docMap["instanceId"] = instance.instanceId
                        docMap["endDt"] = instance.instanceEndDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[dateFormat] = docList
        }
        return durationMap
    }

    private fun getPeriodHour(
        instanceList: List<WfInstanceEntity>,
        dateFormatList: MutableList<String>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (dateFormat in dateFormatList) {
            val docList = mutableListOf<HashMap<String, Any>>()
            instanceList.forEach { instance ->
                val docYear = instance.instanceEndDt!!.year
                val docMonth = instance.instanceEndDt!!.monthValue
                val docDays = instance.instanceEndDt!!.dayOfMonth
                val docHours = instance.instanceEndDt!!.hour
                when (docYear.toString() + this.addStringFormat(docMonth) + docDays + this.addStringFormat(docHours)) {
                    dateFormat -> {
                        val docMap = HashMap<String, Any>()
                        docMap["instanceId"] = instance.instanceId
                        docMap["endDt"] = instance.instanceEndDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[dateFormat] = docList
        }
        return durationMap
    }

    /**
     * 수집한 신청서에 대하여 1차로 duration의 digit와 Unit에 대하여 분리
     * Stacked Column, Basic Line 차트의 경우 2차로 periodUnit 에 따라 데이터를 분리하여 JsonObject의 형태로 구현.
     * Pie 차트의 경우, 설정한 operation Unit을 periodUnit으로 설정하여 데이터에 대한 분리를 진행.
     */
    private fun getDurationDoc(
        chart: ChartDto,
        instanceList: List<WfInstanceEntity>
    ): Map<String, Any> {
        val chartDateTime = this.getChartDateTime(chart)
        val selectDocList = mutableListOf<WfInstanceEntity>()
        var durationMap = LinkedHashMap<String, Any>()
        val dateFormatList = mutableListOf<String>()

        instanceList.forEach { instance ->
            if (instance.instanceEndDt!!.withNano(0) >= chartDateTime.startDateTime) {
                selectDocList.add(instance)
            }
        }
        when (chart.chartConfig.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                durationMap = this.getPeriodYear(chartDateTime, instanceList)
            }
            ChartConstants.Unit.MONTH.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    dateFormatList.add(
                        chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth)
                    )
                    when (chartDateTime.startMonth) {
                        12 -> {
                            chartDateTime.startYear++
                            chartDateTime.startMonth = 1
                        }
                        else -> {
                            chartDateTime.startMonth++
                        }
                    }
                }
                durationMap = this.getPeriodMonth(instanceList, dateFormatList)
            }
            ChartConstants.Unit.DATE.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    val dateFormat = chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth)
                    val lengthOfMonth =
                        YearMonth.from(chartDateTime.startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in chartDateTime.startDays until lengthOfMonth + 1) {
                        dateFormatList.add(dateFormat + this.addStringFormat(day))

                        if (day == chartDateTime.endDateTime!!.dayOfMonth &&
                            chartDateTime.startMonth == chartDateTime.endDateTime.monthValue) {
                            break
                        }
                    }

                    when (chartDateTime.startMonth) {
                        12 -> {
                            chartDateTime.startYear++
                            chartDateTime.startMonth = 1
                        }
                        else -> {
                            chartDateTime.startMonth++
                        }
                    }
                    chartDateTime.startDays = 1
                }
                durationMap = this.getPeriodDate(instanceList, dateFormatList)
            }
            ChartConstants.Unit.HOUR.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    val dateFormat = chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth)
                    val lengthOfMonth =
                        YearMonth.from(chartDateTime.startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in chartDateTime.startDays until lengthOfMonth + 1) {
                        for (hours in chartDateTime.startHours until 25) {
                            dateFormatList.add(dateFormat + day + this.addStringFormat(hours))
                            if (hours == chartDateTime.endDateTime!!.hour &&
                                day == chartDateTime.endDateTime.dayOfMonth &&
                                chartDateTime.startMonth == chartDateTime.endDateTime.monthValue) {
                                break
                            }
                        }
                        if (day == chartDateTime.endDateTime!!.dayOfMonth &&
                            chartDateTime.startMonth == chartDateTime.endDateTime.monthValue) {
                            break
                        }
                        chartDateTime.startHours = 1
                    }

                    when (chartDateTime.startMonth) {
                        12 -> {
                            chartDateTime.startYear++
                            chartDateTime.startMonth = 1
                        }
                        else -> {
                            chartDateTime.startMonth++
                        }
                    }
                    chartDateTime.startDays = 1
                }
                durationMap = this.getPeriodHour(instanceList, dateFormatList)
            }
        }

        val durationDoc = HashMap<String, Any>()
        durationDoc["instanceList"] = durationMap

        return durationDoc
    }

    /**
     * getDurationDoc 함수를 통해 가져온 JsonObject 데이터에 대하여, operation에 대한 계산을 진행
     */
    private fun calculateOperation(durationDoc: Map<String, Any>): LinkedHashMap<String, Any> {
        val operation = LinkedHashMap<String, Any>()
        var totalCount = 0
        val documentListMap: Map<String, Any> =
            mapper.convertValue(durationDoc["instanceList"], object : TypeReference<Map<String, Any>>() {})
        documentListMap.entries.forEach {
            val durationMap: List<Map<String, Any>> =
                mapper.convertValue(it.value, object : TypeReference<List<Map<String, Any>>>() {})
            totalCount += durationMap.size
            val map = LinkedHashMap<String, Any>()
            map[ChartConstants.Operation.COUNT.code] = durationMap.size
            map[ChartConstants.Operation.PERCENT.code] = if (totalCount != 0) {
                ((durationMap.size / totalCount) * 100).toString() + "%"
            } else {
                "0%"
            }
            operation[it.key] = map
        }

        return operation
    }

    /**
     * 문서 분리에 기준이 되는 시작일을 구한다.
     * 종료일은 00시 00분 기준이므로 검색 시 포함하지 않아야 한다.
     */
    private fun getChartDateTime(chart: ChartDto): ChartDateTimeDto {
        var startDateTime: LocalDateTime = LocalDateTime.now()
        var endDateTime: LocalDateTime = LocalDateTime.now()

        when (chart.chartConfig.range.type) {
            ChartConstants.RangeType.BETWEEN.code -> {
                startDateTime = chart.chartConfig.range.from!!.atStartOfDay()
                endDateTime = chart.chartConfig.range.to!!.plusDays(1).atStartOfDay()
            }
            ChartConstants.RangeType.LAST_MONTH.code -> {
                val lastMonth = YearMonth.from(LocalDateTime.now().minusMonths(1))
                startDateTime = lastMonth.atDay(1).atStartOfDay()
                endDateTime = lastMonth.atEndOfMonth().plusDays(1).atStartOfDay()
            }
            ChartConstants.RangeType.LAST_DAY.code -> {
                startDateTime = LocalDate.now().minusDays(1).atStartOfDay()
                endDateTime = LocalDate.now().atStartOfDay()
            }
            ChartConstants.RangeType.ALL.code -> {
                startDateTime = LocalDate.of(1975, 6, 23).atStartOfDay() // 내 생일이다. 문제 있을까? By Jung Hee Chan
                endDateTime = LocalDateTime.now()
            }
        }

        return ChartDateTimeDto(
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            startYear = startDateTime.year,
            startMonth = startDateTime.monthValue,
            startDays = startDateTime.dayOfMonth,
            startHours = startDateTime.hour,
            period = ChronoUnit.MONTHS.between(startDateTime, endDateTime).toInt()
        )
    }

    private fun addStringFormat(target: Int): String {
        return String.format("%02d", target)
    }
}
