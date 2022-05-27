/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricStatus.dto.MetricStatusChartCondition
import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import org.springframework.http.ResponseEntity
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metric-status")
class MetricStatusRestController(
    private val metricStatusService: MetricStatusService
) {
    /**
     * 지표별 SLA 현황 차트 데이터
     */
    @GetMapping("")
    fun getMetricStatusChartData(metricStatusChartCondition: MetricStatusChartCondition, model: Model): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricStatusService.getMetricStatusChartData(metricStatusChartCondition))
    }
}
