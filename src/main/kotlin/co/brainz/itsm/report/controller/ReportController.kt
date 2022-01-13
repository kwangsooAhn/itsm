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
    private val reportTemplatePage: String = "report/reportTemplate"
    private val templatePreviewPage: String = "report/reportTemplatePreview"
    private val reportSearchPage: String = "report/reportSearch"
    private val reportListPage: String = "report/reportList"
    private val reportViewPage: String = "report/reportView"
    private val basicReportSearchPage: String = "report/basicReportSearch"
    private val dashboardTemplateSearch: String = "report/dashboardTemplateSearch"
    private val dashboardManagement: String = "report/dashboardManagementEdit"

    @GetMapping("/customTemplate/search")
    fun getReportTemplateSearch(request: HttpServletRequest, model: Model): String {
        return templateSearchPage
    }

    @GetMapping("/customTemplate")
    fun getReportTemplates(reportTemplateCondition: ReportTemplateCondition, model: Model): String {
        val result = reportTemplateService.getReportTemplateList(reportTemplateCondition)
        model.addAttribute("templateList", result.data)
        model.addAttribute("paging", result.paging)
        return templateListPage
    }

    @GetMapping("/customTemplate/new")
    fun getReportTemplateNew(model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        return reportTemplatePage
    }

    @GetMapping("/customTemplate/{templateId}/edit")
    fun getReportTemplateEdit(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", reportTemplateService.getReportTemplateDetail(templateId))
        return reportTemplatePage
    }

    @GetMapping("/customTemplate/{templateId}/view")
    fun getReportTemplateView(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("view", true)
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", reportTemplateService.getReportTemplateDetail(templateId))
        return reportTemplatePage
    }

    @GetMapping("/customTemplate/preview")
    fun getReportTemplatePreview(model: Model): String {
        model.addAttribute("time", LocalDateTime.now())
        return templatePreviewPage
    }

    @GetMapping("/customReport/search")
    fun getReportSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("categoryList", reportService.getDistinctReportCategoryList())
        return reportSearchPage
    }

    @GetMapping("/customReport")
    fun getReports(reportSearchCondition: ReportSearchCondition, model: Model): String {
        val result = reportService.getReportList(reportSearchCondition)
        model.addAttribute("reportList", result.data)
        model.addAttribute("paging", result.paging)
        return reportListPage
    }

    @GetMapping("/customReport/{reportId}/view")
    fun getReportView(@PathVariable reportId: String, model: Model): String {
        model.addAttribute("report", reportService.getReportDetail(reportId))
        return reportViewPage
    }

    @GetMapping("/basicReport/search")
    fun getBasicReportSearch(request: HttpServletRequest, model: Model): String {
        return  basicReportSearchPage
    }

    @GetMapping("/dashboardTemplate/search")
    fun getDashboardTemplateSearch(request: HttpServletRequest, model: Model): String {
        return dashboardTemplateSearch
    }

    @GetMapping("/dashboardManagement/edit")
    fun getDashboardManagementList(request: HttpServletRequest,model: Model): String {
        return dashboardManagement
    }
}
