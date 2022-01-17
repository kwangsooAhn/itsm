/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.controller

import co.brainz.itsm.statistic.customChart.service.CustomChartService
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition
import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import co.brainz.itsm.statistic.customReport.dto.CustomTemplateCondition
import co.brainz.itsm.statistic.customReport.service.CustomReportService
import co.brainz.itsm.statistic.customReport.service.CustomTemplateService
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/statistics")
class CustomReportController(
    private val customTemplateService: CustomTemplateService,
    private val customReportService: CustomReportService,
    private val customChartService: CustomChartService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val templateSearchPage: String = "statistic/customReportTemplate/customReportTemplateSearch"
    private val templateListPage: String = "statistic/customReportTemplate/customReportTemplateList"
    private val reportTemplatePage: String = "statistic/customReportTemplate/customReportTemplate"
    private val templatePreviewPage: String = "statistic/customReportTemplate/customReportTemplatePreview"
    private val reportSearchPage: String = "statistic/customReport/customReportSearch"
    private val reportListPage: String = "statistic/customReport/customReportList"
    private val reportViewPage: String = "statistic/customReport/customReportView"
    private val basicReportSearchPage: String = "statistic/basicReport/basicReportSearch"

    @GetMapping("/customReportTemplate/search")
    fun getCustomTemplateSearch(request: HttpServletRequest, model: Model): String {
        return templateSearchPage
    }

    @GetMapping("/customReportTemplate")
    fun getCustomTemplates(customTemplateCondition: CustomTemplateCondition, model: Model): String {
        val result = customTemplateService.getReportTemplateList(customTemplateCondition)
        model.addAttribute("templateList", result.data)
        model.addAttribute("paging", result.paging)
        return templateListPage
    }

    @GetMapping("/customReportTemplate/new")
    fun getCustomTemplateNew(model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("chartList", customChartService.getCharts(ChartSearchCondition()).data)
        return reportTemplatePage
    }

    @GetMapping("/customReportTemplate/{templateId}/edit")
    fun getCustomTemplateEdit(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("chartList", customChartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", customTemplateService.getReportTemplateDetail(templateId))
        return reportTemplatePage
    }

    @GetMapping("/customReportTemplate/{templateId}/view")
    fun getCustomTemplateView(@PathVariable templateId: String, model: Model): String {
        model.addAttribute("view", true)
        model.addAttribute("chartList", customChartService.getCharts(ChartSearchCondition()).data)
        model.addAttribute("template", customTemplateService.getReportTemplateDetail(templateId))
        return reportTemplatePage
    }

    @GetMapping("/customReportTemplate/preview")
    fun getCustomTemplatePreview(model: Model): String {
        model.addAttribute("time", LocalDateTime.now())
        return templatePreviewPage
    }

    @GetMapping("/customReport/search")
    fun getCustomReportSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("categoryList", customReportService.getDistinctReportCategoryList())
        return reportSearchPage
    }

    @GetMapping("/customReport")
    fun getCustomReports(reportSearchCondition: ReportSearchCondition, model: Model): String {
        val result = customReportService.getReportList(reportSearchCondition)
        model.addAttribute("reportList", result.data)
        model.addAttribute("paging", result.paging)
        return reportListPage
    }

    @GetMapping("/customReport/{reportId}/view")
    fun getCustomReportView(@PathVariable reportId: String, model: Model): String {
        model.addAttribute("report", customReportService.getReportDetail(reportId))
        return reportViewPage
    }

    @GetMapping("/basicReport/search")
    fun getBasicReportSearch(request: HttpServletRequest, model: Model): String {
        return  basicReportSearchPage
    }
}
