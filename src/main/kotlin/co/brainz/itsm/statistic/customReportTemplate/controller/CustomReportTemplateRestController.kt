/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReportTemplate.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.statistic.customReport.service.CustomReportService
import co.brainz.itsm.statistic.customReportTemplate.service.CustomTemplateService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
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
class CustomReportTemplateRestController(
    private val customTemplateService: CustomTemplateService,
    private val customReportService: CustomReportService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/customReportTemplate")
    fun createReportTemplate(@RequestBody templateData: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customTemplateService.saveReportTemplate(templateData))
    }

    @PutMapping("/customReportTemplate/{templateId}")
    fun updateReportTemplate(
        @PathVariable templateId: String,
        @RequestBody templateData: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customTemplateService.updateReportTemplate(templateData))
    }

    @DeleteMapping("/customReportTemplate/{templateId}")
    fun deleteReportTemplate(@PathVariable templateId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customTemplateService.deleteReportTemplate(templateId))
    }

    @GetMapping("/customReportTemplate/charts")
    fun getReportTemplateChart(request: HttpServletRequest): ResponseEntity<ZResponse> {
        val chartIds = request.getParameterValues("chartId")
        return ZAliceResponse.response(customTemplateService.getReportTemplateChart(chartIds))
    }

    @PostMapping("/customReportTemplate/{templateId}")
    fun saveReport(@PathVariable templateId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(customReportService.saveReport(templateId))
    }
}
