/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.controller

import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.service.MetricYearService
import javax.servlet.http.HttpServletRequest
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
    private val metricYearSearchPage: String = "sla/metricAnnual/management/yearSearch"
    private val metricYearListPage: String = "sla/metricAnnual/management/yearList"
    private val metricYearPage: String = "sla/metricAnnual/management/year"
    private val metricAnnualPage: String = "sla/metricAnnual/annual/statusAnnualSearch"
    private val metricAnnualList: String = "sla/metricAnnual/annual/statusAnnualList"

    /**
     * 연도별 SLA 지표 관리 - 검색 화면 호출
     */
    @GetMapping("/search")
    fun getMetricPoolSearch(): String {
        return metricYearSearchPage
    }

    /**
     * 연도별 SLA 지표 관리 - 리스트 화면 호출
     */
    @GetMapping("")
    fun getMetricPools(metricYearSearchCondition: MetricYearSearchCondition, model: Model): String {
        val result = metricYearService.getMetrics(metricYearSearchCondition)
        model.addAttribute("metricYearsList", result.data)
        model.addAttribute("paging", result.paging)
        return metricYearListPage
    }

    /**
     * 연도별 SLA 지표 관리 - 신규 등록 화면 호출
     */
    @GetMapping("/new")
    fun getMetricPools(): String {
        return metricYearPage
    }

    /**
     * 년도별 SLA 현황 화면 호출
     */
    @GetMapping("/annual/search")
    fun getMetricAnnualSearch(request: HttpServletRequest, model: Model): String {
        return metricAnnualPage
    }

    /**
     * 년도별 SLA 현황 리스트 호출
     */
    @GetMapping("/annual","")
    fun getMetricAnnualList(metricYearSearchCondition: MetricYearSearchCondition, model: Model): String {
        val result = metricYearService.findMetricAnnualSearch(metricYearSearchCondition)
        model.addAttribute("metricYearsList", result.data)
        model.addAttribute("paging", result.paging)
        return metricAnnualList
    }
}
