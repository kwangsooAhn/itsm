/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartCondition
import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import javax.servlet.http.HttpServletRequest
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metrics/status")
class MetricStatusController(
    private val metricStatusService: MetricStatusService
) {
    private val metricStatusSearchPage: String = "sla/metricStatus/statusMetricSearch"
    private val metricStatusChartPage: String = "sla/metricStatus/statusMetricChart"

    @GetMapping("/search")
    fun getMetricStatusSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("metricList", metricStatusService.getMetricList())
        return metricStatusSearchPage
    }

    /**
     * 지표별 SLA 현황 차트 데이터
     */
    @GetMapping("")
    fun getMetricStatusChartData(metricStatusChartCondition: MetricStatusChartCondition, model: Model): String {
        model.addAttribute("chartData", metricStatusService.getMetricStatusChartData(metricStatusChartCondition))
        return metricStatusChartPage
    }
}
