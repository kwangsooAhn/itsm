/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.controller

import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.service.MetricYearService
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.servlet.http.HttpServletRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/sla/metrics")
class MetricYearController(
    private val metricYearService: MetricYearService,
    private val currentSessionUser: CurrentSessionUser
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val metricYearSearchPage: String = "sla/metricAnnual/management/yearSearch"
    private val metricYearListPage: String = "sla/metricAnnual/management/yearList"
    private val metricYearPage: String = "sla/metricAnnual/management/year"
    private val metricAnnualPage: String = "sla/metricAnnual/status/statusAnnualSearch"
    private val metricAnnualListPage: String = "sla/metricAnnual/status/statusAnnualList"

    /**
     * 연도별 SLA 지표 관리 - 검색 화면 호출
     */
    @GetMapping("/search")
    fun getMetricYearSearch(): String {
        return metricYearSearchPage
    }

    /**
     * 연도별 SLA 지표 관리 - 리스트 화면 호출
     */
    @GetMapping("")
    fun getMetricYears(metricYearSearchCondition: MetricYearSearchCondition, model: Model): String {
        val result = metricYearService.getMetrics(metricYearSearchCondition)
        val thisYear = DateTimeFormatter.ofPattern("yyyy")
            .format(AliceUtil().changeTimeBasedTimezone(LocalDateTime.now(), currentSessionUser.getTimezone()))
        model.addAttribute("thisYear", thisYear)
        model.addAttribute("metricYearsList", result.data)
        model.addAttribute("paging", result.paging)
        return metricYearListPage
    }

    /**
     * 연도별 SLA 지표 관리 - 신규 등록 화면 호출
     */
    @GetMapping("/new")
    fun getMetricYearNew(): String {
        return metricYearPage
    }

    /**
     * 연도별 SLA 지표 관리 - 편집 화면 호출
     */
    @GetMapping("/{metricId}/{year}/edit")
    fun getMetricYearEdit(@PathVariable metricId: String, @PathVariable year: String, model: Model): String {
        model.addAttribute("metric", metricYearService.getMetricYearDetail(metricId, year))
        model.addAttribute("edit", true)
        return metricYearPage
    }

    /**
     * 연도별 SLA 지표 관리 - 조회 화면 호출
     */
    @GetMapping("/{metricId}/{year}/view")
    fun getMetricYearView(@PathVariable metricId: String, @PathVariable year: String, model: Model): String {
        model.addAttribute("metric", metricYearService.getMetricYearDetail(metricId, year))
        model.addAttribute("view", true)
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
    @GetMapping("/annual")
    fun getMetricAnnualList(metricYearSearchCondition: MetricYearSearchCondition, model: Model): String {
        val result = metricYearService.findMetricAnnualSearch(metricYearSearchCondition)
        model.addAttribute("metricYearsList", result.data)
        model.addAttribute("paging", result.paging)
        return metricAnnualListPage
    }
}
