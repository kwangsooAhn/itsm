/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.controller

import co.brainz.itsm.chart.dto.ChartSearchCondition
import co.brainz.itsm.chart.service.ChartService
import co.brainz.itsm.report.dto.ReportSearchCondition
import co.brainz.itsm.report.dto.ReportTemplateCondition
import co.brainz.itsm.report.service.ReportService
import co.brainz.itsm.report.service.ReportTemplateService
import java.time.LocalDateTime
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
    private val reportService: ReportService,
    private val chartService: ChartService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val templateSearchPage: String = "report/reportTemplateSearch"
    private val templateListPage: String = "report/reportTemplateList"
    private val templateEditPage: String = "report/reportTemplateEdit"
    private val templateViewPage: String = "report/reportTemplateView"
    private val templatePreviewPage: String = "report/reportTemplatePreview"
    private val reportSearchPage: String = "report/reportSearch"
    private val reportListPage: String = "report/reportList"
    private val reportViewPage: String = "report/reportView"

    @GetMapping("/template/search")
    fun getReportTemplateSearch(request: HttpServletRequest, model: Model): String {
        return templateSearchPage
    }

    @GetMapping("/template")
    fun getReportTemplates(reportTemplateCondition: ReportTemplateCondition, model: Model): String {
        val result = reportTemplateService.getReportTemplateList(reportTemplateCondition)
        model.addAttribute("templateList", result.data)
        model.addAttribute("paging", result.paging)
        return templateListPage
    }

    @GetMapping("/template/new")
    fun getReportTemplateNew(model: Model): String {
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        return templateEditPage
    }

    @GetMapping("/template/{templateId}/edit")
    fun getReportTemplateEdit(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", reportTemplateService.getReportTemplateDetail(templateId))
        return templateEditPage
    }

    @GetMapping("/template/{templateId}/view")
    fun getReportTemplateView(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", reportTemplateService.getReportTemplateDetail(templateId))
        return templateViewPage
    }

    @GetMapping("/template/preview")
    fun getReportTemplatePreview(model: Model): String {
        model.addAttribute("time", LocalDateTime.now())
        return templatePreviewPage
    }

    @GetMapping("/report/search")
    fun getReportSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("categoryList", reportService.getDistinctReportCategoryList())
        return reportSearchPage
    }

    @GetMapping("/report")
    fun getReports(reportSearchCondition: ReportSearchCondition, model: Model): String {
        val result = reportService.getReportList(reportSearchCondition)
        model.addAttribute("reportList", result.data)
        model.addAttribute("paging", result.paging)
        return reportListPage
    }

    @GetMapping("/report/{reportId}/view")
    fun getReportView(@PathVariable reportId: String, model: Model): String {
        model.addAttribute("report", reportService.getReportDetail(reportId))
        return reportViewPage
    }
}
