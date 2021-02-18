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
            targetLabel = targetLabel,
            operation = operation,
            durationDigit = durationDigit.toInt(),
            durationUnit = durationUnit,
            query = makeChartQuery(chart.chartType, targetLabel)
        )

        when (chart.chartType) {
            ChartConstants.Type.STACKEDCOLUMN.code, ChartConstants.Type.BASICLINE.code -> {
                var periodUnit = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.PERIODUNIT.property).asString
                chartDto.periodUnit = periodUnit
                if (chart.chartType == ChartConstants.Type.BASICLINE.code) {
                    var group = chartConfig.asJsonObject.get(ChartConstants.ObjProperty.GROUP.property).asString
                    chartDto.group = group
                }
            }
        }

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
    fun makeChartQuery(chartType: String, labelKey: String): String {
        // 특정 key(label_key Column) 값을 가진 LabelList 조회
        val labelList = aliceLabelRepository.findLabelKeys(labelKey)

        // labelList에서 label_target_id만 추출.
        val labelTargetIds = mutableListOf<String>()
        labelList.forEach { label ->
            labelTargetIds.add(
                label.labelTargetId
            )
        }

        // 위에서 추출된 labelTargetList에 대한 컴포넌트 리스트 조회, 컴포넌트 리스트의 formId만 추출.
        // (awf_label 테이블의 label_target_id == wf_component 테이블의 component_id 인 데이터)
        val componentList = wfComponentRepository.findByComponentIdIn(labelTargetIds)
        val formIds = mutableListOf<String>()
        componentList.forEach { component ->
            // 해당 컴포넌트의 Component_type이 Label인지 유효성 검사 진행
            if (component.componentType == WfComponentConstants.ComponentType.LABEL.code) {
                formIds.add(
                    component.form.formId
                )
            }
        }

        // 위에서 추출된 formId에 대한 form 조회
        val documentList = mutableListOf<WfDocumentEntity>()
        val formList = wfFormRepository.findByFormIdIn(formIds)
        formList.forEach { form ->
            // 해당 form의 form_status가 'form.status.use', 'form.status.publish' 상태인지 유효성 검사 진행
            if (form.formStatus == WfFormConstants.FormStatus.USE.value ||
                form.formStatus == WfFormConstants.FormStatus.PUBLISH.value
            ) {
                // form_status가 'form.status.use', 'form.status.publish'인 form에 대하여 해당 문서양식을 사용하고 있는 신청서를 조회
                form.document?.forEach { document ->
                    // document_status가 'document.status.use' 이며, document_type이 'application-form'인 신청서 중
                    if (document.documentStatus == WfDocumentConstants.Status.USE.code &&
                        document.documentType == DocumentConstants.DocumentType.APPLICATION_FORM.value
                    ) {
                        // 관련된 document의 wf_instance테이블의 instance상태가 'finish'인 document를 documentList에 저장
                        document.instance?.forEach { instance ->
                            if (instance.instanceStatus == WfInstanceConstants.Status.FINISH.code) {
                                documentList.add(document)
                            }
                        }
                    }
                }
            }
        }

        when (chartType) {
            ChartConstants.Type.STACKEDCOLUMN.code -> {
                //TODO TYPE, FROM, OPERATION, DURATION, PERRIODUNIT
            }
            ChartConstants.Type.BASICLINE.code -> {
                //TODO TYPE, FROM, OPERATION, GROUP, DURATION, PERIODUNIT
            }
            ChartConstants.Type.PIE.code -> {
                //TODO TYPE, FORM, OPERATION, DURATION
            }
        }
        return ""
    }
}
