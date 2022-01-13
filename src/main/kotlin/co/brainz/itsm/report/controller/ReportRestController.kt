/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.controller

import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.report.service.ReportService
import co.brainz.itsm.report.service.ReportTemplateService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/reports")
class ReportRestController(
    private val reportTemplateService: ReportTemplateService,
    private val reportService: ReportService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/customTemplate")
    fun createReportTemplate(@RequestBody templateData: String): RestTemplateReturnDto {
        return reportTemplateService.saveReportTemplate(templateData)
    }

    @PutMapping("/customTemplate/{templateId}")
    fun updateReportTemplate(
        @PathVariable templateId: String,
        @RequestBody templateData: String
    ): RestTemplateReturnDto {
        return reportTemplateService.updateReportTemplate(templateData)
    }

    @DeleteMapping("/customTemplate/{templateId}")
    fun deleteReportTemplate(@PathVariable templateId: String): RestTemplateReturnDto {
        return reportTemplateService.deleteReportTemplate(templateId)
    }

    @GetMapping("/customTemplate/charts")
    fun getReportTemplateChart(request: HttpServletRequest): List<ChartDto> {
        val chartIds = request.getParameterValues("chartId")
        return reportTemplateService.getReportTemplateChart(chartIds)
    }

    // 임시적으로 보고서를 생성하는 URL 추가
    @PostMapping("/customTemplate/{templateId}")
    fun saveReport(@PathVariable templateId: String): String {
        return reportService.saveReport(templateId)
    }
}
