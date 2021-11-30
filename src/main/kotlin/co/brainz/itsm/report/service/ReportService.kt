/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.chart.dto.ChartConfig
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.respository.ChartRepository
import co.brainz.itsm.chart.service.ChartManagerFactory
import co.brainz.itsm.report.constants.ReportConstants
import co.brainz.itsm.report.dto.ReportCategoryDto
import co.brainz.itsm.report.dto.ReportDto
import co.brainz.itsm.report.dto.ReportListReturnDto
import co.brainz.itsm.report.dto.ReportSearchCondition
import co.brainz.itsm.report.entity.ReportDataEntity
import co.brainz.itsm.report.entity.ReportEntity
import co.brainz.itsm.report.repository.ReportDataRepository
import co.brainz.itsm.report.repository.ReportRepository
import co.brainz.itsm.report.repository.ReportTemplateRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ReportService(
    private val reportRepository: ReportRepository,
    private val reportDataRepository: ReportDataRepository,
    private val reportTemplateRepository: ReportTemplateRepository,
    private val chartManagerFactory: ChartManagerFactory,
    private val chartRepository: ChartRepository,
    private val aliceTagService: AliceTagService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getReportList(reportSearchCondition: ReportSearchCondition): ReportListReturnDto {
        val queryResult = reportRepository.getReportList(reportSearchCondition)
        return ReportListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = reportRepository.count(),
                currentPageNum = reportSearchCondition.pageNum,
                totalPageNum = Math.ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble())
                    .toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    fun getDistinctReportCategoryList(): List<ReportCategoryDto> {
        return reportRepository.getDistinctReportCategoryList()
    }

    fun getReportDetail(reportId: String): ReportDto {
        val reportEntity = reportRepository.getOne(reportId)

        // 저장된 테이블에서 차트 정보를 조회하여 가져온다 >>> chartDto를 만든다.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val chartDataList = mutableListOf<ChartDto>()
        val reportDataEntities = reportDataRepository.getReportDataEntitiesByReport(reportEntity)
        reportDataEntities.forEach { data ->

            // awf_report_data.values 의 config, propertyJson 정보를 분리하여 chartDto를 설정한다.
            val map = mapper.readValue(data.values, LinkedHashMap::class.java)
            val chart = mapper.convertValue(map["chart"], LinkedHashMap::class.java)
            val configStr = mapper.writeValueAsString(chart["config"])

            val chartDto = ChartDto(
                chartId = data.chartId!!,
                chartName = chart["name"] as String,
                chartType = chart["type"] as String,
                chartDesc = chart["desc"] as String,
                chartConfig = mapper.readValue(configStr, ChartConfig::class.java),
                chartConfigStr = configStr,
                createDt = reportEntity.publishDt,
                tags = aliceTagService.getTagsByTargetId(AliceTagConstants.TagType.CHART.code, data.chartId)
            )

            chartDataList.add(chartManagerFactory.getChartManager(chartDto.chartType).getChart(chartDto))
        }

        return ReportDto(
            reportId = reportEntity.reportId,
            reportName = reportEntity.reportName,
            reportDesc = reportEntity.reportDesc,
            publishDt = reportEntity.publishDt,
            data = chartDataList
        )
    }

    @Transactional
    fun saveReport(templateId: String): String {
        var resultCode = ReportConstants.ReportCreateStatus.STATUS_SUCCESS.code;
        // 스케줄러에 의해 실행되어 awf_report_data 에 저장하는 기능
        val templateEntity = reportTemplateRepository.getOne(templateId)

        // new report
        // 보고서명이 존재하면 보고서 명을 사용하고 없으면 템플릿 명을 사용한다.
        val reportName = if (templateEntity.reportName.isNullOrEmpty()) templateEntity.templateName else templateEntity.reportName

        try {
            var reportEntity = ReportEntity(
                reportId = "",
                reportName = reportName as String,
                reportDesc = templateEntity.templateDesc,
                publishDt = LocalDateTime.now(),
                templateId = templateEntity.templateId
            )
            reportEntity = reportRepository.save(reportEntity)

            // chart list
            templateEntity.charts?.forEach { it ->
                val chartEntity = chartRepository.findChartEntityByChartId(it.chartId)

                // values 에 저장할 값 셋팅
                if (chartEntity != null) {
                    val chartInfo = LinkedHashMap<String, Any>()
                    chartInfo["name"] = chartEntity.chartName
                    chartInfo["type"] = chartEntity.chartType
                    chartInfo["desc"] = chartEntity.chartDesc ?: ""
                    chartInfo["config"] = mapper.readValue(chartEntity.chartConfig, Map::class.java)

                    val valuesMap = LinkedHashMap<String, Any>()
                    valuesMap["chart"] = chartInfo
                    val strValues = mapper.writeValueAsString(valuesMap)
                    val reportDataEntity = ReportDataEntity(
                        dataId = "",
                        report = reportEntity,
                        chartId = chartEntity.chartId,
                        displayOrder = it.displayOrder,
                        values = strValues
                    )
                    reportDataRepository.save(reportDataEntity)
                }
            }
        } catch (e: Exception) {
            resultCode = ReportConstants.ReportCreateStatus.STAUTS_FAIL.code
            e.printStackTrace()
        }

        return resultCode
    }
}
