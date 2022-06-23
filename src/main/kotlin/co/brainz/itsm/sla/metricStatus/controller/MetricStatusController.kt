/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import co.brainz.itsm.sla.metricYear.service.MetricYearService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metric-status")
class MetricStatusController(
    private val metricStatusService: MetricStatusService,
    private val metricYearService: MetricYearService
) {
    private val metricStatusSearchPage: String = "sla/metricStatus/statusMetricSearch"
    private val metricStatusChartPage: String = "sla/metricStatus/statusMetricChart"

    /**
     * 지표별 SLA 현황 검색 화면
     */
    @GetMapping("/search")
    fun getMetricStatusSearch(request: HttpServletRequest, model: Model): String {
        val yearList = metricYearService.getYears()
        model.addAttribute("yearsList", yearList)
        model.addAttribute("metricList", metricStatusService.getMetricList(yearList.firstOrNull()?: ""))
        return metricStatusSearchPage
    }

    /**
     * 지표별 SLA 현황 차트 화면
     */
    @GetMapping("")
    fun getMetricStatusChart(request: HttpServletRequest, model: Model): String {
        return metricStatusChartPage
    }
}
