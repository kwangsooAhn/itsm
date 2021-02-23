/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

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

        var chartConfig = JsonParser().parse(chart.chartConfig)
        val targetLabel = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.FROM.property).asString
        val operation = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.OPERATION.property).asString
        val duration = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.DURATION.property)
        val durationDigit =
            duration.asJsonObject.get(ChartConstants.ObjProperty.DIGIT.property).asString
        val durationUnit =
            duration.asJsonObject.get(ChartConstants.ObjProperty.UNIT.property).asString

        var chartDto = ChartDto(
            chartId = chart.chartId,
            chartType = chart.chartType,
            chartName = chart.chartName,
            chartDesc = chart.chartDesc,
            chartConfig = chart.chartConfig,
            createDt = chart.createDt,
            targetLabel = targetLabel,
            operation = operation,
            durationDigit = durationDigit.toLong(),
            durationUnit = durationUnit
        )

        when (chart.chartType) {
            ChartConstants.Type.STACKEDCOLUMN.code, ChartConstants.Type.BASICLINE.code -> {
                var periodUnit = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.PERIODUNIT.property).asString
                chartDto.periodUnit = periodUnit
                if (chart.chartType == ChartConstants.Type.BASICLINE.code) {
                    if (chartConfig.asJsonObject.get(ChartConstants.ObjProperty.GROUP.property) != null) {
                        var group = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.GROUP.property).asString
                        chartDto.group = group
                    }
                }
            }
        }
        chartDto.query = makeChartQuery(chartDto)

        return chartDto
    }

    /**
     * 통계 차트 등록 / 수정
     */
    fun saveChart(chartDto: ChartDto): String {
        var status = ChartConstants.Status.STATUS_SUCCESS.code
        var chartConfigObj = JsonObject()
        var durationObj = JsonObject()
        var chartFromList = JsonArray()
        chartFromList.add(chartDto.targetLabel)
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
            ChartConstants.Type.STACKEDCOLUMN.code, ChartConstants.Type.BASICLINE.code -> {
                chartConfigObj.addProperty(ChartConstants.ObjProperty.PERIODUNIT.property, chartDto.periodUnit)
                if (chartDto.chartType == ChartConstants.Type.BASICLINE.code) {
                    chartConfigObj.addProperty(ChartConstants.ObjProperty.PERIODUNIT.property, chartDto.periodUnit)
                }
            }
        }

        val chartEntity = ChartEntity(
            chartId = chartDto.chartId,
            chartType = chartDto.chartType,
            chartName = chartDto.chartName,
            chartDesc = chartDto.chartDesc,
            chartConfig = chartConfigObj.toString()
        )

        chartRepository.save(chartEntity)
        return status
    }

    /**
     * 통계 차트 삭제
     */
    fun deleteChart(chartId: String): String {
        var status = ChartConstants.Status.STATUS_SUCCESS.code

        chartRepository.deleteById(chartId)
        return status
    }

    /**
     * 차트 쿼리 생성
     */
    fun makeChartQuery(chart: ChartDto): JsonArray {
        val labelList = aliceLabelRepository.findLabelKeys(chart.targetLabel)
        val labelTargetIds = mutableListOf<String>()
        val formIds = mutableListOf<String>()
        val documentList = mutableListOf<WfDocumentEntity>()
        var jsonObject = JsonObject()
        var jsonObjectArray = JsonArray()

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

        val parsingDocObject = selectDurationDoc(chart, documentList)
        jsonObject.addProperty("title", chart.chartName)
        jsonObject.add("operation", calculateOperation(chart, parsingDocObject))
        jsonObjectArray.add(jsonObject)
        jsonObjectArray.add(parsingDocObject)

        return jsonObjectArray
    }

    fun calculateOperation(chart: ChartDto, parsingDocObject: JsonObject): JsonObject {
        var operationListObject = JsonObject()
        var totalCount = 0
        when (chart.chartType) {
            ChartConstants.Type.STACKEDCOLUMN.code, ChartConstants.Type.BASICLINE.code -> {
                var keyList = parsingDocObject.getAsJsonObject("documentList")
                keyList.entrySet().forEach {
                    totalCount += it.value.asJsonArray.size()
                }
                keyList.entrySet().forEach {
                    var operationObject = JsonObject()
                    operationObject.addProperty("count", it.value.asJsonArray.size())
                    operationObject.addProperty(
                        "percent",
                        if (totalCount != 0) {
                            (((it.value.asJsonArray.size() / totalCount) * 100).toString() + "%")
                        } else {
                            "0%"
                        }
                    )
                    operationListObject.add(it.key, operationObject)
                }
            }
            ChartConstants.Type.PIE.code -> {
                var keyList = parsingDocObject.getAsJsonArray("documentList")
                operationListObject.addProperty("count", keyList.size())
            }
        }
        return operationListObject
    }

    /**
     * 수집한 신청서에 대하여 duration과 Unit과 duration digit, periodUnit 에 따라 데이터를 분리하여 JsonObject의 형태로 구현.
     */
    fun selectDurationDoc(
        chart: ChartDto,
        documentList: MutableList<WfDocumentEntity>
    ): JsonObject {
        lateinit var startDateTime: LocalDateTime
        val selectDocList = mutableListOf<WfDocumentEntity>()
        val endDateTime: LocalDateTime? = chart.createDt
        var jsonObject = JsonObject()
        var jsonDocListObject = JsonObject()
        var dateFormatList = mutableListOf<String>()

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

        when (chart.chartType) {
            ChartConstants.Type.STACKEDCOLUMN.code, ChartConstants.Type.BASICLINE.code -> {
                when (chart.periodUnit) {
                    ChartConstants.Unit.YEAR.code -> {
                        for (year in startDateTime.year until endDateTime!!.year + 1) {
                            var jsonArray = JsonArray()
                            documentList.forEach { document ->
                                when (document.createDt!!.year) {
                                    year -> {
                                        var jsonDocObject = JsonObject()
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
                                    startMonth = 1;
                                }
                                else -> {
                                    startMonth++;
                                }
                            }
                        }

                        for (dateFormat in dateFormatList) {
                            var jsonArray = JsonArray()
                            documentList.forEach { document ->
                                var docYear = document.createDt!!.year
                                var docMonth = document.createDt!!.monthValue
                                var docDateFormat = docYear.toString() + String.format("%02d", docMonth)

                                when (docDateFormat) {
                                    dateFormat -> {
                                        var jsonDocObject = JsonObject()
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
                            var dateFormat = startYear.toString() + String.format("%02d", startMonth)
                            val lengthOfMonth = YearMonth.from(startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                            for (day in startDays until lengthOfMonth + 1) {
                                dateFormatList.add(dateFormat + String.format("%02d", day))

                                if (day == endDateTime!!.dayOfMonth && startMonth == endDateTime!!.monthValue) {
                                    break;
                                }
                            }

                            when (startMonth) {
                                12 -> {
                                    startYear++
                                    startMonth = 1;
                                }
                                else -> {
                                    startMonth++;
                                }
                            }
                            startDays = 1
                        }

                        for (dateFormat in dateFormatList) {
                            var jsonArray = JsonArray()
                            documentList.forEach { document ->
                                var docYear = document.createDt!!.year
                                var docMonth = document.createDt!!.monthValue
                                var docDays = document.createDt!!.dayOfMonth
                                var docDateFormat =
                                    docYear.toString() + String.format("%02d", docMonth) + String.format(
                                        "%02d",
                                        docDays
                                    )


                                when (docDateFormat) {
                                    dateFormat -> {
                                        var jsonDocObject = JsonObject()
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
                            var dateFormat = startYear.toString() + String.format("%02d", startMonth)
                            val lengthOfMonth = YearMonth.from(startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                            for (day in startDays until lengthOfMonth + 1) {
                                for (hours in startHours until 25) {
                                    dateFormatList.add(dateFormat + day + String.format("%02d", hours))
                                    if (hours == endDateTime!!.hour && day == endDateTime!!.dayOfMonth && startMonth == endDateTime!!.monthValue) {
                                        break;
                                    }
                                }
                                if (day == endDateTime!!.dayOfMonth && startMonth == endDateTime!!.monthValue) {
                                    break;
                                }
                                startHours = 1
                            }

                            when (startMonth) {
                                12 -> {
                                    startYear++
                                    startMonth = 1;
                                }
                                else -> {
                                    startMonth++;
                                }
                            }
                            startDays = 1
                        }

                        for (dateFormat in dateFormatList) {
                            var jsonArray = JsonArray()
                            documentList.forEach { document ->
                                var docYear = document.createDt!!.year
                                var docMonth = document.createDt!!.monthValue
                                var docDays = document.createDt!!.dayOfMonth
                                var docHours = document.createDt!!.hour
                                var docDateFormat =
                                    docYear.toString() + String.format(
                                        "%02d",
                                        docMonth
                                    ) + docDays + String.format("%02d", docHours)


                                when (docDateFormat) {
                                    dateFormat -> {
                                        var jsonDocObject = JsonObject()
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
            }
            ChartConstants.Type.PIE.code -> {
                var jsonArray = JsonArray()
                documentList.forEach { document ->
                    var jsonDocObject = JsonObject()
                    jsonDocObject.addProperty("documentId", document.documentId)
                    jsonDocObject.addProperty("documentName", document.documentName)
                    jsonDocObject.addProperty("createDt", document.createDt.toString())
                    jsonArray.add(jsonDocObject)
                }
                jsonDocListObject.add("documentList", jsonArray)
            }
        }
        return jsonDocListObject
    }
}
