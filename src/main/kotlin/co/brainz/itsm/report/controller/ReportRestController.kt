/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.controller

import co.brainz.cmdb.dto.RestTemplateReturnDto
import co.brainz.itsm.report.dto.ReportTemplateDto
import co.brainz.itsm.report.service.ReportTemplateService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/reports")
class ReportRestController(
    private val reportTemplateService: ReportTemplateService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping("/template")
    fun createReportTemplate(@RequestBody templateData: String): RestTemplateReturnDto {
        return reportTemplateService.saveReportTemplate(templateData)
    }

    @PutMapping("/template/{templateId}")
    fun updateReportTemplate(
        @PathVariable templateId: String,
        @RequestBody templateData: String
    ): RestTemplateReturnDto {
        return reportTemplateService.updateReportTemplate(templateData)
    }

    @DeleteMapping("/template/{templateId}")
    fun deleteReportTemplate(@PathVariable templateId: String): RestTemplateReturnDto {
        return reportTemplateService.deleteReportTemplate(templateId)
    }

}
