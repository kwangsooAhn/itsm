/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.controller

import co.brainz.itsm.sla.metricYear.dto.MetricSearchCondition
import co.brainz.itsm.sla.metricYear.service.MetricYearService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metrics")
class MetricYearController(
    private val metricYearService: MetricYearService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val metricSearchPage: String = "sla/metric/metricSearch"
    private val metricListPage: String = "sla/metric/metricList"

    /**
     * 연도별 SLA 지표 관리 - 검색 화면 호출
     */
    @GetMapping("/search")
    fun getMetricPoolSearch(): String {
        return metricSearchPage
    }

    /**
     * 연도별 SLA 지표 관리 - 리스트 화면 호출
     */
    @GetMapping("")
    fun getMetricPools(metricSearchCondition: MetricSearchCondition, model: Model): String {
        val result = metricYearService.getMetrics(metricSearchCondition)
        model.addAttribute("metricYearsList", result.data)
        model.addAttribute("paging", result.paging)
        return metricListPage
    }
}
