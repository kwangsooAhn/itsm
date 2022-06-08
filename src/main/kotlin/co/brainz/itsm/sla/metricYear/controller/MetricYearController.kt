/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.controller

import co.brainz.framework.util.AliceUtil
import co.brainz.framework.util.CurrentSessionUser
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
import org.springframework.web.bind.annotation.RequestParam

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
    private val metricCopyPage: String = "sla/metricAnnual/management/yearCopyModal"

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
    fun getMetricYears(@RequestParam year: String, model: Model): String {
        val thisYear = DateTimeFormatter.ofPattern("yyyy")
            .format(AliceUtil().changeTimeBasedTimezone(LocalDateTime.now(), currentSessionUser.getTimezone()))
        val result = metricYearService.getMetrics(year)
        model.addAttribute("thisYear", thisYear)
        model.addAttribute("metricYearsList", result.data)
        model.addAttribute("totalCount", result.totalCount)
        model.addAttribute("totalCountWithoutCondition", result.totalCountWithoutCondition)
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
    fun getMetricAnnualList(@RequestParam year: String, model: Model): String {
        model.addAttribute("metricYearsList", metricYearService.findMetricAnnualSearch(year))
        return metricAnnualListPage
    }

    /**
     * 연도별 SLA 지표관리 - 복사하기 모달 호출
     */
    @GetMapping("/copy")
    fun getMetricCopy(@RequestParam target: String, model: Model): String {
        model.addAttribute("target", target)
        model.addAttribute("yearsList", metricYearService.getYears())
        return metricCopyPage
    }
}
