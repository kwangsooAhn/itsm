package co.brainz.itsm.statistic.customReportTemplate.controller

import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition
import co.brainz.itsm.statistic.customChart.service.CustomChartService
import co.brainz.itsm.statistic.customReportTemplate.dto.CustomReportTemplateCondition
import co.brainz.itsm.statistic.customReportTemplate.service.CustomTemplateService
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
class CustomReportTemplateController(
    private val customTemplateService: CustomTemplateService,
    private val customChartService: CustomChartService

) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val templateSearchPage: String = "statistic/customReportTemplate/customReportTemplateSearch"
    private val templateListPage: String = "statistic/customReportTemplate/customReportTemplateList"
    private val reportTemplatePage: String = "statistic/customReportTemplate/customReportTemplate"
    private val templatePreviewPage: String = "statistic/customReportTemplate/customReportTemplatePreview"

    @GetMapping("/customReportTemplate/search")
    fun getCustomTemplateSearch(request: HttpServletRequest, model: Model): String {
        return templateSearchPage
    }

    @GetMapping("/customReportTemplate")
    fun getCustomTemplates(customReportTemplateCondition: CustomReportTemplateCondition, model: Model): String {
        val result = customTemplateService.getReportTemplateList(customReportTemplateCondition)
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

}