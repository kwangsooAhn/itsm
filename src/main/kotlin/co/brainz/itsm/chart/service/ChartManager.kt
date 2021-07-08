/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartDateTimeDto
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.instance.constants.WfInstanceConstants
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory

abstract class ChartManager(
    private val chartManagerService: ChartManagerService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    lateinit var chartConfig: Map<String, Any>

    fun getChartConfigStr(chartDto: ChartDto): String {
        val charFormList = mutableListOf<String>()
        chartDto.targetTags?.forEach { tag ->
            charFormList.add(tag.trim())
        }

        val chartMap = LinkedHashMap<String, Any?>()
        chartMap[ChartConstants.ObjProperty.TYPE.property] = chartDto.chartType
        chartMap[ChartConstants.ObjProperty.FROM.property] = charFormList
        chartMap[ChartConstants.ObjProperty.OPERATION.property] = chartDto.operation

        val durationMap = LinkedHashMap<String, Any>()
        durationMap[ChartConstants.ObjProperty.DIGIT.property] = chartDto.durationDigit
        durationMap[ChartConstants.ObjProperty.UNIT.property] = chartDto.durationUnit
        chartMap[ChartConstants.ObjProperty.DURATION.property] = durationMap

        chartMap.putAll(this.setChartConfigDetail((chartDto)))

        return mapper.writeValueAsString(chartMap)
    }

    abstract fun setChartConfigDetail(chartDto: ChartDto): LinkedHashMap<String, Any?>

    fun getChart(chartDto: ChartDto): ChartDto {
        this.chartConfig = this.getChartConfigMap(chartDto.chartConfig!!)
        chartDto.targetTags = this.getChartTargetTags(chartConfig)
        chartDto.operation = this.chartConfig[ChartConstants.ObjProperty.OPERATION.property] as String
        chartDto.durationDigit = this.getChartDurationDigit(chartConfig)
        chartDto.durationUnit = this.getChartDurationUnit(chartConfig)
        this.setChartDetail(chartDto)
        chartDto.propertyJson = this.getChartProperty(chartDto)
        return chartDto
    }

    abstract fun setChartDetail(chartDto: ChartDto): ChartDto

    private fun getChartConfigMap(chartConfig: String): Map<String, Any> {
        return mapper.readValue(chartConfig, object : TypeReference<Map<String, Any>>() {})
    }

    private fun getChartDurationDigit(chartConfig: Map<String, Any>): Long {
        val durationMap = this.getChartDuration(chartConfig)
        return durationMap[ChartConstants.ObjProperty.DIGIT.property].toString().toLong()
    }

    private fun getChartDurationUnit(chartConfig: Map<String, Any>): String {
        val durationMap = this.getChartDuration(chartConfig)
        return durationMap[ChartConstants.ObjProperty.UNIT.property] as String
    }

    private fun getChartTargetTags(chartConfig: Map<String, Any>): ArrayList<String> {
        return mapper.convertValue(
            chartConfig[ChartConstants.ObjProperty.FROM.property],
            object : TypeReference<ArrayList<String>>() {})
    }

    private fun getChartDuration(chartConfig: Map<String, Any>): Map<String, Any> {
        return mapper.convertValue(
            chartConfig[ChartConstants.ObjProperty.DURATION.property],
            object : TypeReference<Map<String, Any>>() {})
    }

    /**
     * 차트 생성 관련 JsonArray 생성
     */
    private fun getChartProperty(chart: ChartDto): String {
        val formIds = mutableListOf<String>()
        val documentList = mutableListOf<WfDocumentEntity>()
        val tagTargetIds = mutableListOf<String>()
        chart.targetTags?.let {
            chartManagerService.getTagValueList(AliceTagConstants.TagType.COMPONENT.code, it).forEach { tag ->
                tagTargetIds.add(tag.targetId)
            }
        }
        val componentList = chartManagerService.getComponentList(tagTargetIds)
        componentList.forEach { component ->
            formIds.add(
                component.form.formId
            )
        }

        val formList = chartManagerService.getFormList(formIds)
        formList.forEach { form ->
            if (form.formStatus == WfFormConstants.FormStatus.USE.value ||
                form.formStatus == WfFormConstants.FormStatus.PUBLISH.value
            ) {
                form.document?.forEach { document ->
                    if (document.documentStatus == WfDocumentConstants.Status.USE.code &&
                        document.documentType == DocumentConstants.DocumentType.APPLICATION_FORM.value
                    ) {
                        document.instance?.forEach { instance ->
                            if (instance.instanceStatus == WfInstanceConstants.Status.FINISH.code) {
                                documentList.add(document)
                            }
                        }
                    }
                }
            }
        }

        val propertyList = mutableListOf<Map<String, Any>>()
        val durationDoc = this.getDurationDoc(chart, documentList)
        val map = LinkedHashMap<String, Any>()
        map["title"] = chart.chartName
        map["operation"] = this.calculateOperation(durationDoc)
        propertyList.add(map)
        propertyList.add(durationDoc)

        return mapper.writeValueAsString(propertyList)
    }

    private fun getPeriodYear(
        chartDateTime: ChartDateTimeDto,
        documentList: MutableList<WfDocumentEntity>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (year in chartDateTime.startYear until chartDateTime.endDateTime!!.year + 1) {
            val docList = mutableListOf<HashMap<String, Any>>()
            documentList.forEach { document ->
                when (document.createDt!!.year) {
                    year -> {
                        val docMap = HashMap<String, Any>()
                        docMap["documentId"] = document.documentId
                        docMap["documentName"] = document.documentName
                        docMap["createDt"] = document.createDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[year.toString()] = docList
        }
        return durationMap
    }

    private fun getPeriodMonth(
        documentList: MutableList<WfDocumentEntity>,
        dateFormatList: MutableList<String>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (dateFormat in dateFormatList) {
            val docList = mutableListOf<HashMap<String, Any>>()
            documentList.forEach { document ->
                val docYear = document.createDt!!.year
                val docMonth = document.createDt!!.monthValue
                val docDateFormat = docYear.toString() + this.addStringFormat(docMonth)
                when (docDateFormat) {
                    dateFormat -> {
                        val docMap = HashMap<String, Any>()
                        docMap["documentId"] = document.documentId
                        docMap["documentName"] = document.documentName
                        docMap["createDt"] = document.createDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[dateFormat] = docList
        }
        return durationMap
    }

    private fun getPeriodDate(
        documentList: MutableList<WfDocumentEntity>,
        dateFormatList: MutableList<String>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (dateFormat in dateFormatList) {
            val docList = mutableListOf<HashMap<String, Any>>()
            documentList.forEach { document ->
                val docYear = document.createDt!!.year
                val docMonth = document.createDt!!.monthValue
                val docDays = document.createDt!!.dayOfMonth
                val docDateFormat =
                    docYear.toString() + this.addStringFormat(docMonth) + this.addStringFormat(docDays)
                when (docDateFormat) {
                    dateFormat -> {
                        val docMap = HashMap<String, Any>()
                        docMap["documentId"] = document.documentId
                        docMap["documentName"] = document.documentName
                        docMap["createDt"] = document.createDt.toString()
                        docList.add(docMap)
                    }
                }
            }
            durationMap[dateFormat] = docList
        }
        return durationMap
    }

    private fun getPeriodHour(
        documentList: MutableList<WfDocumentEntity>,
        dateFormatList: MutableList<String>
    ): LinkedHashMap<String, Any> {
        val durationMap = LinkedHashMap<String, Any>()
        for (dateFormat in dateFormatList) {
            val docList = mutableListOf<HashMap<String, Any>>()
            documentList.forEach { document ->
                val docYear = document.createDt!!.year
                val docMonth = document.createDt!!.monthValue
                val docDays = document.createDt!!.dayOfMonth
                val docHours = document.createDt!!.hour
                val docDateFormat =
                    docYear.toString() + this.addStringFormat(docMonth) + docDays + this.addStringFormat(docHours)
                when (docDateFormat) {
                    dateFormat -> {
                        val docMap = HashMap<String, Any>()
                        docMap["documentId"] = document.documentId
                        docMap["documentName"] = document.documentName
                        docMap["createDt"] = document.createDt.toString()
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
        documentList: MutableList<WfDocumentEntity>
    ): Map<String, Any> {
        val chartDateTime = this.getChartDateTime(chart)
        val selectDocList = mutableListOf<WfDocumentEntity>()
        var durationMap = LinkedHashMap<String, Any>()
        val dateFormatList = mutableListOf<String>()

        documentList.forEach { document ->
            if (document.createDt!!.withNano(0) >= chartDateTime.startDateTime) {
                selectDocList.add(document)
            }
        }
        when (chart.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                durationMap = this.getPeriodYear(chartDateTime, documentList)
            }
            ChartConstants.Unit.MONTH.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    dateFormatList.add(chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth))
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
                durationMap = this.getPeriodMonth(documentList, dateFormatList)
            }
            ChartConstants.Unit.DATE.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    val dateFormat = chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth)
                    val lengthOfMonth =
                        YearMonth.from(chartDateTime.startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in chartDateTime.startDays until lengthOfMonth + 1) {
                        dateFormatList.add(dateFormat + this.addStringFormat(day))

                        if (day == chartDateTime.endDateTime!!.dayOfMonth && chartDateTime.startMonth == chartDateTime.endDateTime.monthValue) {
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
                durationMap = this.getPeriodDate(documentList, dateFormatList)
            }
            ChartConstants.Unit.HOUR.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    val dateFormat = chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth)
                    val lengthOfMonth =
                        YearMonth.from(chartDateTime.startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in chartDateTime.startDays until lengthOfMonth + 1) {
                        for (hours in chartDateTime.startHours until 25) {
                            dateFormatList.add(dateFormat + day + this.addStringFormat(hours))
                            if (hours == chartDateTime.endDateTime!!.hour && day == chartDateTime.endDateTime.dayOfMonth && chartDateTime.startMonth == chartDateTime.endDateTime.monthValue) {
                                break
                            }
                        }
                        if (day == chartDateTime.endDateTime!!.dayOfMonth && chartDateTime.startMonth == chartDateTime.endDateTime.monthValue) {
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
                durationMap = this.getPeriodHour(documentList, dateFormatList)
            }
        }

        val durationDoc = HashMap<String, Any>()
        durationDoc["documentList"] = durationMap

        return durationDoc
    }

    /**
     * getDurationDoc 함수를 통해 가져온 JsonObject 데이터에 대하여, operation에 대한 계산을 진행
     */
    private fun calculateOperation(durationDoc: Map<String, Any>): LinkedHashMap<String, Any> {
        val operation = LinkedHashMap<String, Any>()
        var totalCount = 0
        val documentListMap: Map<String, Any> =
            mapper.convertValue(durationDoc["documentList"], object : TypeReference<Map<String, Any>>() {})
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
     */
    private fun getChartDateTime(chart: ChartDto): ChartDateTimeDto {
        lateinit var startDateTime: LocalDateTime
        val endDateTime: LocalDateTime? = chart.createDt

        when (chart.durationUnit) {
            ChartConstants.Unit.YEAR.code -> {
                startDateTime = endDateTime!!.minusYears(chart.durationDigit)
            }
            ChartConstants.Unit.MONTH.code -> {
                startDateTime = endDateTime!!.minusMonths(chart.durationDigit)
            }
            ChartConstants.Unit.DATE.code -> {
                startDateTime = endDateTime!!.minusDays(chart.durationDigit)
            }
            ChartConstants.Unit.HOUR.code -> {
                startDateTime = endDateTime!!.minusHours(chart.durationDigit)
            }
        }

        return ChartDateTimeDto(
            startDateTime = startDateTime,
            endDateTime = chart.createDt,
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
