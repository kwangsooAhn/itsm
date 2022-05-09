/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.controller

import co.brainz.itsm.statistic.customReport.dto.ReportSearchCondition
import co.brainz.itsm.statistic.customReport.service.CustomReportService
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
    private val customReportService: CustomReportService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    private val reportSearchPage: String = "statistic/customReport/customReportSearch"
    private val reportListPage: String = "statistic/customReport/customReportList"
    private val reportViewPage: String = "statistic/customReport/customReportView"

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
}
