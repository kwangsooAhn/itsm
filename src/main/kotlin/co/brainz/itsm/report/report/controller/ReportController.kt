/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.report.controller

import co.brainz.itsm.report.chart.dto.ChartSearchCondition
import co.brainz.itsm.report.chart.service.ChartService
import co.brainz.itsm.report.report.dto.ReportSearchCondition
import co.brainz.itsm.report.report.dto.ReportTemplateCondition
import co.brainz.itsm.report.report.service.ReportService
import co.brainz.itsm.report.report.service.ReportTemplateService
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

    private val templateSearchPage: String = "report/report/customTemplateSearch"
    private val templateListPage: String = "report/report/customTemplateList"
    private val reportTemplatePage: String = "report/report/customTemplate"
    private val templatePreviewPage: String = "report/report/customTemplatePreview"
    private val reportSearchPage: String = "report/report/customReportSearch"
    private val reportListPage: String = "report/report/customReportList"
    private val reportViewPage: String = "report/report/customReportView"
    private val basicReportSearchPage: String = "report/report/basicReportSearch"

    @GetMapping("/customTemplate/search")
    fun getCustomTemplateSearch(request: HttpServletRequest, model: Model): String {
        return templateSearchPage
    }

    @GetMapping("/customTemplate")
    fun getCustomTemplates(reportTemplateCondition: ReportTemplateCondition, model: Model): String {
        val result = reportTemplateService.getReportTemplateList(reportTemplateCondition)
        model.addAttribute("templateList", result.data)
        model.addAttribute("paging", result.paging)
        return templateListPage
    }

    @GetMapping("/customTemplate/new")
    fun getCustomTemplateNew(model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        return reportTemplatePage
    }

    @GetMapping("/customTemplate/{templateId}/edit")
    fun getCustomTemplateEdit(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", reportTemplateService.getReportTemplateDetail(templateId))
        return reportTemplatePage
    }

    @GetMapping("/customTemplate/{templateId}/view")
    fun getCustomTemplateView(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("view", true)
        model.addAttribute("chartList", chartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", reportTemplateService.getReportTemplateDetail(templateId))
        return reportTemplatePage
    }

    @GetMapping("/customTemplate/preview")
    fun getCustomTemplatePreview(model: Model): String {
        model.addAttribute("time", LocalDateTime.now())
        return templatePreviewPage
    }

    @GetMapping("/customReport/search")
    fun getCustomReportSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("categoryList", reportService.getDistinctReportCategoryList())
        return reportSearchPage
    }

    @GetMapping("/customReport")
    fun getCustomReports(reportSearchCondition: ReportSearchCondition, model: Model): String {
        val result = reportService.getReportList(reportSearchCondition)
        model.addAttribute("reportList", result.data)
        model.addAttribute("paging", result.paging)
        return reportListPage
    }

    @GetMapping("/customReport/{reportId}/view")
    fun getCustomReportView(@PathVariable reportId: String, model: Model): String {
        model.addAttribute("report", reportService.getReportDetail(reportId))
        return reportViewPage
    }

    @GetMapping("/basicReport/search")
    fun getBasicReportSearch(request: HttpServletRequest, model: Model): String {
        return  basicReportSearchPage
    }
}
