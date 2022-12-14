/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.statistic.customReport.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.statistic.customChart.dto.ChartConfig
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.respository.CustomChartRepository
import co.brainz.itsm.statistic.customChart.service.ChartManagerFactory
import co.brainz.itsm.statistic.customReport.dto.CustomReportListDto
import co.brainz.itsm.statistic.customReport.dto.CustomReportListReturnDto
import co.brainz.itsm.statistic.customReport.dto.ReportCategoryDto
import co.brainz.itsm.statistic.customReport.dto.ReportDto
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import co.brainz.itsm.statistic.customReport.entity.CustomReportDataEntity
import co.brainz.itsm.statistic.customReport.entity.ReportEntity
import co.brainz.itsm.statistic.customReport.repository.CusmtomReportRepository
import co.brainz.itsm.statistic.customReport.repository.CustomReportDataRepository
import co.brainz.itsm.statistic.customReportTemplate.repository.CustomReportTemplateRepository
import com.fasterxml.jackson.core.type.TypeReference
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
    private val customReportTemplateRepository: CustomReportTemplateRepository,
    private val chartManagerFactory: ChartManagerFactory,
    private val customChartRepository: CustomChartRepository,
    private val aliceTagService: AliceTagService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getReportList(reportSearchCondition: ReportSearchCondition): CustomReportListReturnDto {
        val pagingResult = customReportRepository.getReportList(reportSearchCondition)
        return CustomReportListReturnDto(
            data = mapper.convertValue(pagingResult.dataList, object : TypeReference<List<CustomReportListDto>>() {}),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = customReportRepository.count(),
                currentPageNum = reportSearchCondition.pageNum,
                totalPageNum = Math.ceil(pagingResult.totalCount.toDouble() / reportSearchCondition.contentNumPerPage.toDouble())
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

        // ????????? ??????????????? ?????? ????????? ???????????? ???????????? >>> chartDto??? ?????????.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val chartDataList = mutableListOf<ChartDto>()
        val reportDataEntities = customReportDataRepository.getReportDataEntitiesByReport(reportEntity)
        reportDataEntities.forEach { data ->

            // awf_report_data.values ??? config, propertyJson ????????? ???????????? chartDto??? ????????????.
            val map = mapper.readValue(data.values, LinkedHashMap::class.java)
            val chart = mapper.convertValue(map["chart"], LinkedHashMap::class.java)
            val configStr = mapper.writeValueAsString(chart["config"])

            val chartDto = data.chartId?.let {
                ChartDto(
                    chartId = it,
                    chartName = chart["name"] as String,
                    chartType = chart["type"] as String,
                    chartDesc = chart["desc"] as String,
                    chartConfig = mapper.readValue(configStr, ChartConfig::class.java),
                    tags = aliceTagService.getTagsByTargetId(AliceTagConstants.TagType.CHART.code, it)
                )
            }
            if (chartDto != null) {
                chartDataList.add(chartManagerFactory.getChartManager(chartDto.chartType).getChart(chartDto))
            }
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
    fun saveReport(templateId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS

        // ??????????????? ?????? ???????????? awf_report_data ??? ???????????? ??????
        val templateEntity = customReportTemplateRepository.getOne(templateId)

        // new report
        // ??????????????? ???????????? ????????? ?????? ???????????? ????????? ????????? ?????? ????????????.
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

                // values ??? ????????? ??? ??????
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
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }

        return ZResponse(
            status = status.code
        )
    }
}
