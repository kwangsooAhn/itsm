/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.service

import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.dto.ChartCalculateAverageDto
import co.brainz.itsm.chart.dto.ChartComponentDataDto
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartDateTimeDto
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.form.service.FormService
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.itsm.token.service.TokenService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceDto
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.YearMonth
import java.time.temporal.ChronoUnit

abstract class ChartManager(
    private val chartManagerService: ChartManagerService,
    private val instanceService: InstanceService,
    private val documentService: DocumentService,
    private val formService: FormService,
    private val tokenService: TokenService,
    private val wfTokenManagerService: WfTokenManagerService
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
        val formIds = mutableListOf<String>()
        val documentList = mutableListOf<WfDocumentEntity>()
        val tagTargetIds = mutableListOf<String>()
        chart.chartConfig.tags?.let {
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
            if (form.formStatus != WfFormConstants.FormStatus.EDIT.value) {
                form.document.forEach { document ->
                    if (document.documentStatus != WfDocumentConstants.Status.TEMPORARY.code) {
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
        map["operation"] = this.calculateOperation(durationDoc, tagTargetIds)
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
                when (docYear.toString() + this.addStringFormat(docMonth)) {
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
                when (docYear.toString() + this.addStringFormat(docMonth) + this.addStringFormat(docDays)) {
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
                when (docYear.toString() + this.addStringFormat(docMonth) + docDays + this.addStringFormat(docHours)) {
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
        when (chart.chartConfig.periodUnit) {
            ChartConstants.Unit.YEAR.code -> {
                durationMap = this.getPeriodYear(chartDateTime, documentList)
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
                durationMap = this.getPeriodMonth(documentList, dateFormatList)
            }
            ChartConstants.Unit.DATE.code -> {
                for (index in 0 until chartDateTime.period + 1) {
                    val dateFormat = chartDateTime.startYear.toString() + this.addStringFormat(chartDateTime.startMonth)
                    val lengthOfMonth =
                        YearMonth.from(chartDateTime.startDateTime.plusMonths(index.toLong())).lengthOfMonth()

                    for (day in chartDateTime.startDays until lengthOfMonth + 1) {
                        dateFormatList.add(dateFormat + this.addStringFormat(day))

                        if (day == chartDateTime.endDateTime!!.dayOfMonth &&
                            chartDateTime.startMonth == chartDateTime.endDateTime.monthValue
                        ) {
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
                            if (hours == chartDateTime.endDateTime!!.hour &&
                                day == chartDateTime.endDateTime.dayOfMonth &&
                                chartDateTime.startMonth == chartDateTime.endDateTime.monthValue
                            ) {
                                break
                            }
                        }
                        if (day == chartDateTime.endDateTime!!.dayOfMonth &&
                            chartDateTime.startMonth == chartDateTime.endDateTime.monthValue
                        ) {
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
    private fun calculateOperation(
        durationDoc: Map<String, Any>,
        tagTargetIds: MutableList<String>
    ): LinkedHashMap<String, Any> {
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
        this.getTagAverageData(tagTargetIds)

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

    /**
     * 인스턴스 데이터에서 태그 평균 계산을 위한 데이터를 추출한다.
     */
    private fun getTagAverageData(tagTargetIds: MutableList<String>): String {
        val instance1 = instanceService.getInstance("6b9955d53d9a4336bbf63db1b2010d8f")

        val instanceList = mutableListOf<RestTemplateInstanceDto>()
        instanceList.add(instance1)


        // 해당 인스턴스와 관련된 신청서(document)의 문서(form)를 찾아 컴포넌트 리스트를 출력한다.
        // 여기서 수집하는 컴포넌트 데이터는 태그와 관련된 컴포넌트이고 'dropdown', 'radio', 'checkBox' 타입의 컴포넌트를 수집한다.
        val componentDataMap = LinkedHashMap<String, MutableList<ChartComponentDataDto>>()
        instanceList.forEach { instance ->
            val componentDataList = mutableListOf<ChartComponentDataDto>()
            val form = documentService.getDocument(instance.documentId).formId
            formService.getFormData(form).group?.forEach { FormGroupDto ->
                FormGroupDto.row.forEach { FormRowDto ->
                    FormRowDto.component.forEach { FormComponentDto ->
                        when (FormComponentDto.type) {
                            WfComponentConstants.ComponentTypeCode.SELECT.code,
                            WfComponentConstants.ComponentTypeCode.RADIO.code,
                            WfComponentConstants.ComponentTypeCode.CHECKBOX.code -> {
                                for (target in tagTargetIds) {
                                    if (target == FormComponentDto.id) {
                                        componentDataList.add(
                                            ChartComponentDataDto(
                                                componentId = FormComponentDto.id,
                                                componentType = FormComponentDto.type
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            componentDataMap[instance.instanceId] = componentDataList
        }

        // 위에서 분류된 인스턴스의 토큰 데이터를 찾고 그 중에서 엘리먼트의 타입이 'commonEnd'인 토큰을 찾는다.
        // 토큰을 찾았으면 wf_token_data 테이블에서 해당 토큰 데이터와 일치하는 컴포넌트의 value 값을 맵핑한다.
        // 이때 평균 계산을 위해 tagValue, instanceId, componentValue 값이 필요하다.
        instanceList.forEach { instance ->
            val componentDataList = mutableListOf<ChartComponentDataDto>()
            tokenService.findTokenByInstanceId(instance.instanceId).forEach { token ->
                if (token.elementType == WfElementConstants.ElementType.COMMON_END_EVENT.value) {
                    componentDataMap.getValue(instance.instanceId).forEach { componentDataDto ->
                        componentDataList.add(
                            ChartComponentDataDto(
                                componentId = componentDataDto.componentId,
                                componentValue = wfTokenManagerService.getComponentValue(
                                    token.tokenId,
                                    componentDataDto.componentId,
                                    null
                                ),
                                componentType = componentDataDto.componentType,
                                tagValue = chartManagerService.getTagsByTargetId(
                                    AliceTagConstants.TagType.COMPONENT.code,
                                    componentDataDto.componentId
                                )[0].tagValue
                            )
                        )
                    }
                }
            }
            componentDataMap[instance.instanceId] = componentDataList
        }

        val tagAverageDataMap = LinkedHashMap<String, Any>()
        for ((key, value) in componentDataMap) {
            val calculationMap = LinkedHashMap<String?, ChartCalculateAverageDto>()
            if (value.size > 0) {
                value.forEach { data ->
                    if (!data.componentValue.isNullOrBlank()) {
                        data.componentValue.split("|").forEach { tagValue ->
                            if (tagValue.toDoubleOrNull() != null) {
                                if (calculationMap[data.tagValue] == null) {
                                    calculationMap[data.tagValue] = ChartCalculateAverageDto(
                                        sum = tagValue.toDouble()
                                    )
                                } else {
                                    calculationMap[data.tagValue] = ChartCalculateAverageDto(
                                        count = calculationMap[data.tagValue]?.count!!.plus(1),
                                        sum = calculationMap[data.tagValue]?.sum!!.plus(tagValue.toDouble())
                                    )
                                }
                            }
                        }
                    }
                    calculationMap[data.tagValue]?.average =
                        (calculationMap[data.tagValue]?.sum!! / calculationMap[data.tagValue]?.count!!)
                }
                tagAverageDataMap[key] = calculationMap
            }
        }
        return "clear"
    }
}
