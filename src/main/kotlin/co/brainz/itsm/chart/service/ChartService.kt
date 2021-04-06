/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.label.entity.AliceLabelEntity
import co.brainz.framework.label.repository.AliceLabelRepository
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.respository.ChartRepository
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.instance.constants.WfInstanceConstants
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChartService(
    private val chartRepository: ChartRepository,
    private val aliceLabelRepository: AliceLabelRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfFormRepository: WfFormRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 통계 차트 조회
     */
    fun getCharts(searchTypeName: String, offset: String): List<ChartListDto> {
        return chartRepository.findChartList(searchTypeName, offset.toLong())
    }

    /**
     * 단일 통계 차트 조회
     */
    fun getChart(chartId: String): ChartDto {
        val chart = chartRepository.getOne(chartId)
        val targetLabels = arrayListOf<String>()
        val chartConfigJson = JsonParser().parse(chart.chartConfig).asJsonObject

        chartConfigJson.get(ChartConstants.ObjProperty.FROM.property).asJsonArray.forEach { label ->
            targetLabels.add(label.asString)
        }
        val operation = chartConfigJson.get(ChartConstants.ObjProperty.OPERATION.property).asString
        val durationDigit =
            chartConfigJson.get(ChartConstants.ObjProperty.DURATION.property).asJsonObject.get(ChartConstants.ObjProperty.DIGIT.property).asString
        val durationUnit =
            chartConfigJson.get(ChartConstants.ObjProperty.DURATION.property).asJsonObject.get(ChartConstants.ObjProperty.UNIT.property).asString

        val chartDto = ChartDto(
            chartId = chart.chartId,
            chartType = chart.chartType,
            chartName = chart.chartName,
            chartDesc = chart.chartDesc,
            chartConfig = chart.chartConfig,
            createDt = chart.createDt,
            targetLabels = targetLabels,
            operation = operation,
            durationDigit = durationDigit.toLong(),
            durationUnit = durationUnit
        )

        when (chart.chartType) {
            ChartConstants.Type.STACKED_COLUMN.code, ChartConstants.Type.BASIC_LINE.code -> {
                chartDto.periodUnit =
                    chartConfigJson.get(ChartConstants.ObjProperty.PERIOD_UNIT.property).asString
                if (chart.chartType == ChartConstants.Type.BASIC_LINE.code) {
                    chartDto.group = chartConfigJson.get(ChartConstants.ObjProperty.GROUP.property)?.asString
                }
            }
        }
        chartDto.propertyJson = getChartProperty(chartDto)

        return chartDto
    }

    /**
     * 통계 차트 등록 / 수정
     */
    fun saveChart(chartDto: ChartDto): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code

        val chartEntity = ChartEntity(
            chartId = chartDto.chartId,
            chartType = chartDto.chartType,
            chartName = chartDto.chartName,
            chartDesc = chartDto.chartDesc,
            chartConfig = getChartConfig(chartDto)
        )

        chartRepository.save(chartEntity)
        return status
    }

    /**
     * 통계 차트 삭제
     */
    fun deleteChart(chartId: String): String {
        val status = ChartConstants.Status.STATUS_SUCCESS.code

        chartRepository.deleteById(chartId)
        return status
    }

    /**
     * 차트 생성 관련 JsonArray 생성
     */
    fun getChartProperty(chart: ChartDto): String {
        val labelList = mutableListOf<AliceLabelEntity>()
        chart.targetLabels?.let {
            aliceLabelRepository.findLabelByKeyIn(it).forEach { aliceLabelEntity ->
                labelList.add(aliceLabelEntity)
            }
        }
        val labelTargetIds = mutableListOf<String>()
        val formIds = mutableListOf<String>()
        val documentList = mutableListOf<WfDocumentEntity>()
        val jsonObject = JsonObject()
        val jsonObjectArray = JsonArray()

        labelList.forEach { label ->
            labelTargetIds.add(
                label.labelTargetId
            )
        }

        val componentList = wfComponentRepository.findByComponentIdIn(labelTargetIds)
        componentList.forEach { component ->
            if (component.componentType == WfComponentConstants.ComponentType.LABEL.code) {
                formIds.add(
                    component.form.formId
                )
            }
        }

        val formList = wfFormRepository.findByFormIdIn(formIds)
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

        val parsingDocObject = getDurationDoc(chart, documentList)
        jsonObject.addProperty("title", chart.chartName)
        jsonObject.add("operation", calculateOperation(chart, parsingDocObject))
        jsonObjectArray.add(jsonObject)
        jsonObjectArray.add(parsingDocObject)

        return jsonObjectArray.toString()
    }

    /**
     * getDurationDoc 함수를 통해 가져온 JsonObject 데이터에 대하여, operation에 대한 계산을 진행
     */
    fun calculateOperation(chart: ChartDto, parsingDocObject: JsonObject): JsonObject {
        val operationListObject = JsonObject()
        var totalCount = 0
        val keyList = parsingDocObject.getAsJsonObject("documentList")

        keyList.entrySet().forEach {
            totalCount += it.value.asJsonArray.size()
        }
        keyList.entrySet().forEach {
            val operationObject = JsonObject()
            operationObject.addProperty(ChartConstants.Operation.COUNT.code, it.value.asJsonArray.size())
            operationObject.addProperty(
                ChartConstants.Operation.PERCENT.code,
                if (totalCount != 0) {
                    (((it.value.asJsonArray.size() / totalCount) * 100).toString() + "%")
                } else {
                    "0%"
                }
            )
            operationListObject.add(it.key, operationObject)
        }

        return operationListObject
    }

    /**
     * 수집한 신청서에 대하여 1차로 duration의 digit와 Unit에 대하여 분리
     * Stacked Column, Basic Line 차트의 경우 2차로 periodUnit 에 따라 데이터를 분리하여 JsonObject의 형태로 구현.
     * Pie 차트의 경우, 설정한 operation Unit을 periodUnit으로 설정하여 데이터에 대한 분리를 진행.
     */
    fun getDurationDoc(
        chart: ChartDto,
        documentList: MutableList<WfDocumentEntity>
    ): JsonObject {
        lateinit var startDateTime: LocalDateTime
        val selectDocList = mutableListOf<WfDocumentEntity>()
        val endDateTime: LocalDateTime? = chart.createDt
        val jsonObject = JsonObject()
        val jsonDocListObject = JsonObject()
        val dateFormatList = mutableListOf<String>()

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
        var startYear = startDateTime.year
        var startMonth = startDateTime.monthValue
        var startDays = startDateTime.dayOfMonth
        var startHours = startDateTime.hour
        val period = ChronoUnit.MONTHS.between(startDateTime, endDateTime).toInt()

        documentList.forEach { document ->
            if (document.createDt!!.withNano(0) >= startDateTime) {
                selectDocList.add(document)
            }
        }

        if (chart.chartType == ChartConstants.Type.PIE.code) {
            chart.periodUnit = chart.durationUnit
        }

        when (chart.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                for (year in startDateTime.year until endDateTime!!.year + 1) {
                    val jsonArray = JsonArray()
                    documentList.forEach { document ->
                        when (document.createDt!!.year) {
                            year -> {
                                val jsonDocObject = JsonObject()
                                jsonDocObject.addProperty("documentId", document.documentId)
                                jsonDocObject.addProperty("documentName", document.documentName)
                                jsonDocObject.addProperty("createDt", document.createDt.toString())
                                jsonArray.add(jsonDocObject)
                            }
                        }
                    }
                    jsonObject.add(year.toString(), jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
            ChartConstants.Unit.MONTH.code -> {
                for (index in 0 until period + 1) {
                    dateFormatList.add(startYear.toString() + String.format("%02d", startMonth))
                    when (startMonth) {
                        12 -> {
                            startYear++
                            startMonth = 1
                        }
                        else -> {
                            startMonth++
                        }
                    }
                }

                for (dateFormat in dateFormatList) {
                    val jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docYear = document.createDt!!.year
                        val docMonth = document.createDt!!.monthValue
                        val docDateFormat = docYear.toString() + String.format("%02d", docMonth)

                        when (docDateFormat) {
                            dateFormat -> {
                                val jsonDocObject = JsonObject()
                                jsonDocObject.addProperty("documentId", document.documentId)
                                jsonDocObject.addProperty("documentName", document.documentName)
                                jsonDocObject.addProperty("createDt", document.createDt.toString())
                                jsonArray.add(jsonDocObject)
                            }
                        }
                    }
                    jsonObject.add(dateFormat, jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
            ChartConstants.Unit.DATE.code -> {
                for (index in 0 until period + 1) {
                    val dateFormat = startYear.toString() + String.format("%02d", startMonth)
                    val lengthOfMonth = YearMonth.from(startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in startDays until lengthOfMonth + 1) {
                        dateFormatList.add(dateFormat + String.format("%02d", day))

                        if (day == endDateTime!!.dayOfMonth && startMonth == endDateTime.monthValue) {
                            break
                        }
                    }

                    when (startMonth) {
                        12 -> {
                            startYear++
                            startMonth = 1
                        }
                        else -> {
                            startMonth++
                        }
                    }
                    startDays = 1
                }

                for (dateFormat in dateFormatList) {
                    val jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docYear = document.createDt!!.year
                        val docMonth = document.createDt!!.monthValue
                        val docDays = document.createDt!!.dayOfMonth
                        val docDateFormat =
                            docYear.toString() + String.format("%02d", docMonth) + String.format(
                                "%02d",
                                docDays
                            )

                        when (docDateFormat) {
                            dateFormat -> {
                                val jsonDocObject = JsonObject()
                                jsonDocObject.addProperty("documentId", document.documentId)
                                jsonDocObject.addProperty("documentName", document.documentName)
                                jsonDocObject.addProperty("createDt", document.createDt.toString())
                                jsonArray.add(jsonDocObject)
                            }
                        }
                    }
                    jsonObject.add(dateFormat, jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
            ChartConstants.Unit.HOUR.code -> {
                for (index in 0 until period + 1) {
                    val dateFormat = startYear.toString() + String.format("%02d", startMonth)
                    val lengthOfMonth = YearMonth.from(startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in startDays until lengthOfMonth + 1) {
                        for (hours in startHours until 25) {
                            dateFormatList.add(dateFormat + day + String.format("%02d", hours))
                            if (hours == endDateTime!!.hour && day == endDateTime.dayOfMonth && startMonth == endDateTime.monthValue) {
                                break
                            }
                        }
                        if (day == endDateTime!!.dayOfMonth && startMonth == endDateTime.monthValue) {
                            break
                        }
                        startHours = 1
                    }

                    when (startMonth) {
                        12 -> {
                            startYear++
                            startMonth = 1
                        }
                        else -> {
                            startMonth++
                        }
                    }
                    startDays = 1
                }

                for (dateFormat in dateFormatList) {
                    val jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docYear = document.createDt!!.year
                        val docMonth = document.createDt!!.monthValue
                        val docDays = document.createDt!!.dayOfMonth
                        val docHours = document.createDt!!.hour
                        val docDateFormat =
                            docYear.toString() + String.format(
                                "%02d",
                                docMonth
                            ) + docDays + String.format("%02d", docHours)

                        when (docDateFormat) {
                            dateFormat -> {
                                val jsonDocObject = JsonObject()
                                jsonDocObject.addProperty("documentId", document.documentId)
                                jsonDocObject.addProperty("documentName", document.documentName)
                                jsonDocObject.addProperty("createDt", document.createDt.toString())
                                jsonArray.add(jsonDocObject)
                            }
                        }
                    }
                    jsonObject.add(dateFormat, jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
        }
        return jsonDocListObject
    }

    /**
     * 화면에서 설정한 옵션에 대한 chartConfig를 생성한다.
     */
    fun getChartConfig(chartDto: ChartDto): String {
        val chartConfigObj = JsonObject()
        val durationObj = JsonObject()
        val chartFromList = JsonArray()

        chartDto.targetLabels?.forEach { label ->
            chartFromList.add(label.trim())
        }
        // type
        chartConfigObj.addProperty(ChartConstants.ObjProperty.TYPE.property, chartDto.chartType)
        // from
        chartConfigObj.add(ChartConstants.ObjProperty.FROM.property, chartFromList)
        // operations
        chartConfigObj.addProperty(ChartConstants.ObjProperty.OPERATION.property, chartDto.operation)
        // duration
        durationObj.addProperty(ChartConstants.ObjProperty.DIGIT.property, chartDto.durationDigit)
        durationObj.addProperty(ChartConstants.ObjProperty.UNIT.property, chartDto.durationUnit)
        chartConfigObj.add(ChartConstants.ObjProperty.DURATION.property, durationObj)
        // periodUnit, group
        when (chartDto.chartType) {
            ChartConstants.Type.STACKED_COLUMN.code, ChartConstants.Type.BASIC_LINE.code -> {
                chartConfigObj.addProperty(ChartConstants.ObjProperty.PERIOD_UNIT.property, chartDto.periodUnit)
                if (chartDto.chartType == ChartConstants.Type.BASIC_LINE.code) {
                    chartConfigObj.addProperty(
                        ChartConstants.ObjProperty.GROUP.property, chartDto.group
                    )
                }
            }
        }

        return chartConfigObj.toString()
    }

    fun getPreviewChart(chartId: String, chartPreviewDto: ChartDto): ChartDto {
        var chart: ChartEntity
        val targetLabels = arrayListOf<String>()
        val chartConfigJson = JsonParser().parse(getChartConfig(chartPreviewDto)).asJsonObject
        chartConfigJson.get(ChartConstants.ObjProperty.FROM.property).asJsonArray.forEach { label ->
            targetLabels.add(label.asString)
        }
        val operation = chartConfigJson.get(ChartConstants.ObjProperty.OPERATION.property).asString
        val durationDigit =
            chartConfigJson.get(ChartConstants.ObjProperty.DURATION.property).asJsonObject.get(ChartConstants.ObjProperty.DIGIT.property).asString
        val durationUnit =
            chartConfigJson.get(ChartConstants.ObjProperty.DURATION.property).asJsonObject.get(ChartConstants.ObjProperty.UNIT.property).asString

        val chartDto = ChartDto(
            chartType = chartPreviewDto.chartType,
            chartName = chartPreviewDto.chartName,
            chartDesc = chartPreviewDto.chartDesc,
            chartConfig = getChartConfig(chartPreviewDto),
            targetLabels = targetLabels,
            operation = operation,
            durationDigit = durationDigit.toLong(),
            durationUnit = durationUnit
        )

        if (chartId != "undefined") {
            chart = chartRepository.getOne(chartId)
            chartDto.chartId = chart.chartId
            chartDto.createDt = chart.createDt
        } else {
            chartDto.createDt = LocalDateTime.now()
        }

        when (chartDto.chartType) {
            ChartConstants.Type.STACKED_COLUMN.code, ChartConstants.Type.BASIC_LINE.code -> {
                chartDto.periodUnit =
                    chartConfigJson.get(ChartConstants.ObjProperty.PERIOD_UNIT.property).asString
                if (chartDto.chartType == ChartConstants.Type.BASIC_LINE.code) {
                    chartDto.group = chartConfigJson.get(ChartConstants.ObjProperty.GROUP.property)?.asString
                }
            }
        }
        chartDto.propertyJson = getChartProperty(chartDto)

        return chartDto
    }
}
