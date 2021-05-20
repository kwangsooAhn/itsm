/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.dto.ChartListReturnDto
import co.brainz.itsm.chart.entity.ChartEntity
import co.brainz.itsm.chart.respository.ChartRepository
import co.brainz.itsm.document.constants.DocumentConstants
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
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ChartService(
    private val chartRepository: ChartRepository,
    private val aliceTagRepository: AliceTagRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfFormRepository: WfFormRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 전체 사용자 정의 차트 조회
     */
    fun getCharts(searchTypeName: String, offset: String): ChartListReturnDto {
        return chartRepository.findChartList(searchTypeName, offset.toLong())
    }

    /**
     * 단일 사용자 정의 차트 조회 및 미리보기 조회
     */
    fun getChart(chartId: String, chartPreviewDto: ChartDto?): ChartDto {
        lateinit var chartDto: ChartDto
        val chartEntity = chartRepository.findByIdOrNull(chartId)

        if (chartPreviewDto == null) {
            if (chartEntity != null) {
                chartDto = ChartDto(
                    chartId = chartEntity.chartId,
                    chartType = chartEntity.chartType,
                    chartName = chartEntity.chartName,
                    chartDesc = chartEntity.chartDesc,
                    chartConfig = chartEntity.chartConfig,
                    createDt = chartEntity.createDt
                )
            }
        } else {
            chartDto = ChartDto(
                chartType = chartPreviewDto.chartType,
                chartName = chartPreviewDto.chartName,
                chartDesc = chartPreviewDto.chartDesc,
                chartConfig = getChartConfig(chartPreviewDto)
            )
            if (chartEntity != null) {
                chartDto.chartId = chartEntity.chartId
                chartDto.createDt = chartEntity.createDt
            } else {
                chartDto.createDt = LocalDateTime.now()
            }
        }

        val targetTags = arrayListOf<String>()
        val chartConfigJson = JsonParser().parse(chartDto.chartConfig).asJsonObject
        chartConfigJson.get(ChartConstants.ObjProperty.FROM.property).asJsonArray.forEach { tag ->
            targetTags.add(tag.asString)
        }
        chartDto.targetTags = targetTags
        chartDto.operation = chartConfigJson.get(ChartConstants.ObjProperty.OPERATION.property).asString
        chartDto.durationDigit =
            chartConfigJson.get(ChartConstants.ObjProperty.DURATION.property).asJsonObject.get(ChartConstants.ObjProperty.DIGIT.property).asString.toLong()
        chartDto.durationUnit =
            chartConfigJson.get(ChartConstants.ObjProperty.DURATION.property).asJsonObject.get(ChartConstants.ObjProperty.UNIT.property).asString

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

    /**
     * 사용자 정의 차트 등록 / 수정
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
     * 사용자 정의 차트 삭제
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
        val formIds = mutableListOf<String>()
        val documentList = mutableListOf<WfDocumentEntity>()
        val jsonObject = JsonObject()
        val jsonObjectArray = JsonArray()
        val tagTargetIds = mutableListOf<String>()
        chart.targetTags?.let {
            aliceTagRepository.findByTagValueIn(AliceTagConstants.TagType.COMPONENT.code, it).forEach { tag ->
                tagTargetIds.add(tag.targetId)
            }
        }

        val componentList = wfComponentRepository.findByComponentIdIn(tagTargetIds)
        componentList.forEach { component ->
            formIds.add(
                component.form.formId
            )
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
        val startDateTime = getStartDateTime(chart)
        val selectDocList = mutableListOf<WfDocumentEntity>()
        val endDateTime: LocalDateTime? = chart.createDt
        val jsonObject = JsonObject()
        val jsonDocListObject = JsonObject()
        val dateFormatList = mutableListOf<String>()

        documentList.forEach { document ->
            if (document.createDt!!.withNano(0) >= startDateTime) {
                selectDocList.add(document)
            }
        }

        if (chart.chartType == ChartConstants.Type.PIE.code) {
            chart.periodUnit = chart.durationUnit
        }

        var startYear = startDateTime.year
        var startMonth = startDateTime.monthValue
        var startDays = startDateTime.dayOfMonth
        var startHours = startDateTime.hour
        val period = ChronoUnit.MONTHS.between(startDateTime, endDateTime).toInt()

        when (chart.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                for (year in startDateTime.year until endDateTime!!.year + 1) {
                    var jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docDateFormat = document.createDt!!.year
                        getDateFormatData(docDateFormat, year, document, jsonArray)
                    }
                    jsonObject.add(year.toString(), jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
            ChartConstants.Unit.MONTH.code -> {
                for (index in 0 until period + 1) {
                    dateFormatList.add(startYear.toString() + addStringFormat(startMonth))
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
                    var jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docYear = document.createDt!!.year
                        val docMonth = document.createDt!!.monthValue
                        val docDateFormat = docYear.toString() + addStringFormat(docMonth)
                        getDateFormatData(docDateFormat, dateFormat, document, jsonArray)
                    }
                    jsonObject.add(dateFormat, jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
            ChartConstants.Unit.DATE.code -> {
                for (index in 0 until period + 1) {
                    val dateFormat = startYear.toString() + addStringFormat(startMonth)
                    val lengthOfMonth = YearMonth.from(startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in startDays until lengthOfMonth + 1) {
                        dateFormatList.add(dateFormat + addStringFormat(day))

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
                    var jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docYear = document.createDt!!.year
                        val docMonth = document.createDt!!.monthValue
                        val docDays = document.createDt!!.dayOfMonth
                        val docDateFormat =
                            docYear.toString() + addStringFormat(docMonth) + addStringFormat(docDays)
                        getDateFormatData(docDateFormat, dateFormat, document, jsonArray)
                    }
                    jsonObject.add(dateFormat, jsonArray)
                }
                jsonDocListObject.add("documentList", jsonObject)
            }
            ChartConstants.Unit.HOUR.code -> {
                for (index in 0 until period + 1) {
                    val dateFormat = startYear.toString() + addStringFormat(startMonth)
                    val lengthOfMonth = YearMonth.from(startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in startDays until lengthOfMonth + 1) {
                        for (hours in startHours until 25) {
                            dateFormatList.add(dateFormat + day + addStringFormat(hours))
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
                    var jsonArray = JsonArray()
                    documentList.forEach { document ->
                        val docYear = document.createDt!!.year
                        val docMonth = document.createDt!!.monthValue
                        val docDays = document.createDt!!.dayOfMonth
                        val docHours = document.createDt!!.hour
                        val docDateFormat =
                            docYear.toString() + addStringFormat(docMonth) + docDays + addStringFormat(docHours)
                        getDateFormatData(docDateFormat, dateFormat, document, jsonArray)
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

        chartDto.targetTags?.forEach { tag ->
            chartFromList.add(tag.trim())
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

    /**
     * 문서 분리에 기준이 되는 시작일을 구한다.
     */
    fun getStartDateTime(chart: ChartDto): LocalDateTime {
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

        return startDateTime
    }

    /**
     * 문서에 대한 정보를 특정 dateFormat에 따라 분리하여 jsonArray에 저장한다.
     */
    fun getDateFormatData(
        docDateFormat: Any,
        dateFormat: Any,
        document: WfDocumentEntity,
        jsonArray: JsonArray
    ): JsonArray {
        when (docDateFormat) {
            dateFormat -> {
                val jsonDocObject = JsonObject()
                jsonDocObject.addProperty("documentId", document.documentId)
                jsonDocObject.addProperty("documentName", document.documentName)
                jsonDocObject.addProperty("createDt", document.createDt.toString())
                jsonArray.add(jsonDocObject)
            }
        }
        return jsonArray
    }

    fun addStringFormat(target: Int): String {
        return String.format("%02d", target)
    }
}
