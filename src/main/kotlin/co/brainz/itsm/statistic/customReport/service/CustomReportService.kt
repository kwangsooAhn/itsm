/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.statistic.customChart.respository.CustomChartRepository
import co.brainz.itsm.statistic.customChart.service.ChartManagerFactory
import co.brainz.itsm.statistic.customReport.constants.CustomReportConstants
import co.brainz.itsm.statistic.customReport.dto.ReportCategoryDto
import co.brainz.itsm.statistic.customReport.dto.ReportDto
import co.brainz.itsm.statistic.customReport.dto.CustomReportListReturnDto
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import co.brainz.itsm.statistic.customReport.entity.CustomReportDataEntity
import co.brainz.itsm.statistic.customReport.entity.ReportEntity
import co.brainz.itsm.statistic.customReport.repository.CustomReportDataRepository
import co.brainz.itsm.statistic.customReport.repository.CusmtomReportRepository
import co.brainz.itsm.statistic.customReport.repository.CustomTemplateRepository
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CustomReportService(
    private val customReportRepository: CusmtomReportRepository,
    private val customReportDataRepository: CustomReportDataRepository,
    private val customTemplateRepository: CustomTemplateRepository,
    private val chartManagerFactory: ChartManagerFactory,
    private val customChartRepository: CustomChartRepository,
    private val aliceTagService: AliceTagService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getReportList(reportSearchCondition: ReportSearchCondition): CustomReportListReturnDto {
        val queryResult = customReportRepository.getReportList(reportSearchCondition)
        return CustomReportListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = customReportRepository.count(),
                currentPageNum = reportSearchCondition.pageNum,
                totalPageNum = Math.ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble())
                    .toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    fun getDistinctReportCategoryList(): List<ReportCategoryDto> {
        return customReportRepository.getDistinctReportCategoryList()
    }

    fun getReportDetail(reportId: String): ReportDto {
        val reportEntity = customReportRepository.getOne(reportId)

        // 저장된 테이블에서 차트 정보를 조회하여 가져온다 >>> chartDto를 만든다.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val chartDataList = mutableListOf<co.brainz.itsm.statistic.customChart.dto.ChartDto>()
        val reportDataEntities = customReportDataRepository.getReportDataEntitiesByReport(reportEntity)
        reportDataEntities.forEach { data ->

            // awf_report_data.values 의 config, propertyJson 정보를 분리하여 chartDto를 설정한다.
            val map = mapper.readValue(data.values, LinkedHashMap::class.java)
            val chart = mapper.convertValue(map["chart"], LinkedHashMap::class.java)
            val configStr = mapper.writeValueAsString(chart["config"])

            val chartDto = co.brainz.itsm.statistic.customChart.dto.ChartDto(
                chartId = data.chartId!!,
                chartName = chart["name"] as String,
                chartType = chart["type"] as String,
                chartDesc = chart["desc"] as String,
                chartConfig = mapper.readValue(configStr, co.brainz.itsm.statistic.customChart.dto.ChartConfig::class.java),
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
        var resultCode = CustomReportConstants.ReportCreateStatus.STATUS_SUCCESS.code
        // 스케줄러에 의해 실행되어 awf_report_data 에 저장하는 기능
        val templateEntity = customTemplateRepository.getOne(templateId)

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
            reportEntity = customReportRepository.save(reportEntity)

            // chart list
            templateEntity.charts?.forEach { it ->
                val chartEntity = customChartRepository.findChartEntityByChartId(it.chartId)

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
                    val reportDataEntity = CustomReportDataEntity(
                        dataId = "",
                        report = reportEntity,
                        chartId = chartEntity.chartId,
                        displayOrder = it.displayOrder,
                        values = strValues
                    )
                    customReportDataRepository.save(reportDataEntity)
                }
            }
        } catch (e: Exception) {
            resultCode = CustomReportConstants.ReportCreateStatus.STAUTS_FAIL.code
            e.printStackTrace()
        }

        return resultCode
    }
}
