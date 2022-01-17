/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.controller

import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customReport.service.CustomReportService
import co.brainz.itsm.statistic.customReport.service.CustomTemplateService
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
@RequestMapping("/rest/statistics")
class CustomReportRestController(
    private val customTemplateService: CustomTemplateService,
    private val customReportService: CustomReportService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/customReportTemplate")
    fun createReportTemplate(@RequestBody templateData: String): RestTemplateReturnDto {
        return customTemplateService.saveReportTemplate(templateData)
    }

    @PutMapping("/customReportTemplate/{templateId}")
    fun updateReportTemplate(
        @PathVariable templateId: String,
        @RequestBody templateData: String
    ): RestTemplateReturnDto {
        return customTemplateService.updateReportTemplate(templateData)
    }

    @DeleteMapping("/customReportTemplate/{templateId}")
    fun deleteReportTemplate(@PathVariable templateId: String): RestTemplateReturnDto {
        return customTemplateService.deleteReportTemplate(templateId)
    }

    @GetMapping("/customReportTemplate/charts")
    fun getReportTemplateChart(request: HttpServletRequest): List<ChartDto> {
        val chartIds = request.getParameterValues("chartId")
        return customTemplateService.getReportTemplateChart(chartIds)
    }

    // 임시적으로 보고서를 생성하는 URL 추가
    @PostMapping("/customReportTemplate/{templateId}")
    fun saveReport(@PathVariable templateId: String): String {
        return customReportService.saveReport(templateId)
    }
}
