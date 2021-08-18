/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.report.service

import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.service.ChartManagerFactory
import co.brainz.itsm.report.dto.ReportDto
import co.brainz.itsm.report.dto.ReportListReturnDto
import co.brainz.itsm.report.dto.ReportSearchDto
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
    private val reportTemplateService: ReportTemplateService,
    private val reportTemplateRepository: ReportTemplateRepository,
    private val chartManagerFactory: ChartManagerFactory
) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    fun getReportList(reportSearchDto: ReportSearchDto): ReportListReturnDto {
        val reportList = reportRepository.getReportList(reportSearchDto)
        return ReportListReturnDto(
            data = reportList.results,
            totalCount = reportList.total
        )
    }

    fun getReportDetail(reportId: String): ReportDto {
        val reportEntity = reportRepository.getOne(reportId)
        val reportDto = ReportDto(
            reportId = reportEntity.reportId,
            reportName = reportEntity.reportName,
            reportDesc = reportEntity.reportDesc,
            publishDt = reportEntity.publishDt
        )

        // template 목록과 데이터를 추가한다.
        val chartDataList = mutableListOf<ChartDto>()
        // 저장된 테이블에서 차트 정보를 조회하여 가져온다 >>> chartDto를 만든다.
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        val reportDataEntities = reportDataRepository.getReportDataEntitiesByReport(reportEntity)
        reportDataEntities.forEach { data ->
            val chartDto = ChartDto(
                chartId = data.chartId!!
            )
            // awf_report_data.values 의 cnofig, propertyJson 정보를 분리하여 chartDto를 설정한다.
            val map = mapper.readValue(data.values, LinkedHashMap::class.java)
            val chart = mapper.convertValue(map["chart"], LinkedHashMap::class.java)

            chartDto.chartName = chart["name"] as String
            chartDto.chartType = chart["type"] as String
            chartDto.chartDesc = chart["desc"] as String
            chartDto.chartConfig = mapper.writeValueAsString(chart["config"])
            chartDto.createDt = reportEntity.publishDt
            chartDataList.add(chartManagerFactory.getChartManager(chartDto.chartType).getChart(chartDto))
        }

        if (chartDataList.isNotEmpty()) {
            reportDto.data = chartDataList
        }

        return reportDto
    }

    @Transactional
    fun saveReport(templateId: String): String {
        // 스케줄러에 의해 실행되어 awf_report_data 에 저장하는 기능
        val templateEntity = reportTemplateRepository.getOne(templateId)

        // new report
        var reportEntity = ReportEntity(
            reportId = "",
            reportName = templateEntity.templateName,
            reportDesc = templateEntity.templateDesc,
            publishDt = LocalDateTime.now(),
            template = templateEntity
        )
        reportEntity = reportRepository.save(reportEntity)

        // chart list
        templateEntity.charts?.forEach { it ->
            val chartEntity = it.chart

            //values에 저장할 값 셋팅
            val chartInfo = LinkedHashMap<String, Any>()
            chartInfo["name"] = chartEntity.chartName
            chartInfo["type"] = chartEntity.chartType
            chartInfo["desc"] = chartEntity.chartDesc?: ""
            chartInfo["config"] = mapper.readValue(chartEntity.chartConfig, Map::class.java)

            val valuesMap = LinkedHashMap<String, Any>()
            valuesMap["chart"] = chartInfo
            val strValues = mapper.writeValueAsString(valuesMap)
            val reportDataEntity = ReportDataEntity(
                dataId = "",
                report = reportEntity,
                chartId = chartEntity.chartId,
                values = strValues
            )
            reportDataRepository.save(reportDataEntity)
        }

        return reportEntity.reportId
    }
}
