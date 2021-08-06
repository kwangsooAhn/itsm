/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.controller

import co.brainz.itsm.report.dto.ReportSearchDto
import co.brainz.itsm.report.dto.ReportTemplateSearchDto
import co.brainz.itsm.report.service.ReportService
import co.brainz.itsm.report.service.ReportTemplateService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/reports")
class ReportController(
    private val reportTemplateService: ReportTemplateService,
    private val reportService: ReportService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val templateSearchPage: String = "report/reportTemplateSearch"
    private val templateListPage: String = "report/reportTemplateList"
    private val templateListFragment: String = "report/reportTemplateList :: list"
    private val templateEditPage: String = "report/reportTemplateEdit"
    private val reportSearchPage: String = "report/reportSearch"
    private val reportListPage: String = "report/reportList"
    private val reportListFragment: String = "report/reportList :: list"
    private val reportViewPage: String = "report/reportView"

    @GetMapping("/template/search")
    fun getReportTemplateSearch(request: HttpServletRequest, model: Model): String {
        return templateSearchPage
    }

    @GetMapping("/template")
    fun getReportTemplates(reportTemplateSearchDto: ReportTemplateSearchDto, model: Model): String {
        val result = reportTemplateService.getReportTemplateList(reportTemplateSearchDto)
        model.addAttribute("templateList", result.data)
        model.addAttribute("templateListCount", result.totalCount)
        return if (reportTemplateSearchDto.isScroll) templateListFragment else templateListPage
    }

    @GetMapping("/template/new")
    fun getReportTemplateNew(model: Model): String {
        return templateEditPage
    }

    @GetMapping("/template/{templateId}/edit")
    fun getReportTemplateEdit(@PathVariable templateId: String): String {
        return templateEditPage
    }

    @GetMapping("/template/{templateId}/view")
    fun getReportTemplateView(@PathVariable templateId: String): String {
        return templateEditPage
    }

    @GetMapping("/report/search")
    fun getReportSearch(request: HttpServletRequest, model: Model): String {
        return reportSearchPage
    }

    @GetMapping("/report")
    fun getReports(reportSearchDto: ReportSearchDto, model: Model): String {
        val result = reportService.getReportList(reportSearchDto)
        model.addAttribute("reportList", result.data)
        model.addAttribute("reportListCount", result.totalCount)
        return if (reportSearchDto.isScroll) reportListFragment else reportListPage
    }

    @GetMapping("/report/{reportId}/view")
    fun getReportView(@PathVariable reportId: String): String {
        return reportViewPage
    }
}
