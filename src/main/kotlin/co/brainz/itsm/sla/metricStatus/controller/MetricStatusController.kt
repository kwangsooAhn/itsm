/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metrics")
class MetricStatusController(
    private val metricStatusService: MetricStatusService
) {
    private val metricYearlyPage: String = "sla/metricStatus/YearliesSearch"
    private val metricYearlyList: String = "sla/metricStatus/YearliesList"

    /**
     * 년도별 SLA 현황 화면
     */
    @GetMapping("/yearlies/search")
    fun getMetricYearlySearch(request: HttpServletRequest, model: Model): String {
        return metricYearlyPage
    }

    /**
     * 년도별 SLA 현황 리스트 조회
     */
    @GetMapping("/yearlies","")
    fun getMetricYearlyList(metricYearSearchCondition: MetricYearSearchCondition, model: Model): String {
        val result = metricStatusService.findMetricYearlySearch(metricYearSearchCondition)
        model.addAttribute("metricYearlyList", result.data)
        model.addAttribute("paging", result.paging)
        return metricYearlyList
    }
}
