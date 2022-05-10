/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.controller

import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricPool.service.MetricPoolService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metric-pool")
class MetricPoolController(
    private val metricPoolService: MetricPoolService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val metricPoolSearchPage: String = "sla/metric-pool/metricPoolSearch"
    private val metricPoolListPage: String = "sla/metric-pool/metricPoolList"

    /**
     * SLA 지표 관리 - 검색 화면 호출
     */
    @GetMapping("/search")
    fun getMetricPoolSearch(): String {
        return metricPoolSearchPage
    }

    /**
     * SLA 지표 관리 - 리스트 화면 호출
     */
    @GetMapping("")
    fun getMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition, model: Model): String {
        val result = metricPoolService.getMetricPools(metricPoolSearchCondition)
        model.addAttribute("metricPoolList", result.data)
        model.addAttribute("paging", result.paging)
        return metricPoolListPage
    }
}
