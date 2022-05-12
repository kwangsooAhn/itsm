/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.controller

import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.service.MetricManualService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metrics/manual")
class MetricManualController(
    private val metricManualService: MetricManualService
) {
    private val manualSearchPage: String = "sla/metric/manual/manualSearch"
    private val metricManualList: String = "sla/metric/manual/manualList"

    /**
     * 수동 지표 검색 화면
     */
    @GetMapping("/search")
    fun getMetricManualSearch(request: HttpServletRequest, model: Model): String {
        return manualSearchPage
    }

    /**
     * 수동 지표 리스트 조회
     */
    @GetMapping("")
    fun getMetricManualList(metricManualSearchCondition: MetricManualSearchCondition, model: Model): String {
        val result = metricManualService.findMetricManualSearch(metricManualSearchCondition)
        model.addAttribute("metricManualList", result.data)
        model.addAttribute("paging", result.paging)
        return metricManualList
    }

}
