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
@RequestMapping("/sla/metric-manuals")
class MetricManualController(
    private val metricManualService: MetricManualService
) {
    private val metricManualSearchPage: String = "sla/metricManual/metricManualSearch"
    private val metricManualListPage: String = "sla/metricManual/metricManualList"
    private val metricManualNewModalPage: String = "sla/metricManual/metricManualNewModal"

    /**
     * 수동 지표 검색 화면
     */
    @GetMapping("/search")
    fun getMetricManualSearch(request: HttpServletRequest, model: Model): String {
        model.addAttribute("metricList", metricManualService.getMetricsByManual())
        return metricManualSearchPage
    }

    /**
     * 수동 지표 리스트 조회
     */
    @GetMapping("")
    fun getMetricManualList(metricManualSearchCondition: MetricManualSearchCondition, model: Model): String {
        val result = metricManualService.findMetricManualSearch(metricManualSearchCondition)
        model.addAttribute("metricManualList", result.data)
        model.addAttribute("paging", result.paging)
        return metricManualListPage
    }

    /**
     * 수동 지표 등록 모달 화면
     */
    @GetMapping("/new")
    fun getMetricManualNew(request: HttpServletRequest, model: Model): String {
        model.addAttribute("metricList", metricManualService.getMetricManuals())
        return metricManualNewModalPage
    }
}
