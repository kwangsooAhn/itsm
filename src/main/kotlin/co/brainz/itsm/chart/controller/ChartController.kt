/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.controller

import co.brainz.itsm.chart.constants.ChartConstants
import co.brainz.itsm.chart.service.ChartService
import co.brainz.itsm.code.service.CodeService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/charts")
class ChartController(
    private val chartService: ChartService,
    private val codeService: CodeService
) {

    private val chartSearchPage: String = "chart/chartSearch"
    private val chartListPage: String = "chart/chartList"
    private val chartListFragment: String = "chart/chartList :: list"
    private val chartEditPage: String = "chart/chartEdit"
    private val chartViewPage: String = "chart/chartView"

    /**
     * 통계 차트 목록 검색 화면 호출
     */
    @GetMapping("/search")
    fun getChartSearch(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.CHART_TYPE_P_CODE))
        return chartSearchPage
    }

    /**
     * 통계 차트 목록 화면 호출
     */
    @GetMapping("")
    fun getCharts(request: HttpServletRequest, model: Model): String {
        val searchTypeName = request.getParameter("searchTypeName")
        val offset = request.getParameter("offset") ?: "0"
        val result = chartService.getCharts(searchTypeName, offset)
        model.addAttribute("chartList", result)
        model.addAttribute("chartListCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return if (request.getParameter("isScroll").toBoolean()) chartListFragment else chartListPage
    }

    /**
     * Chart 등록 화면 호출
     */
    @GetMapping("/new")
    fun getChartNew(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.CHART_TYPE_P_CODE))
        model.addAttribute("operationList", codeService.selectCodeByParent(ChartConstants.CHART_OPERATION_P_CODE))
        model.addAttribute("unitList", codeService.selectCodeByParent(ChartConstants.CHART_UNIT_P_CODE))
        return chartEditPage
    }

    /**
     * Chart 수정 화면 호출
     */
    @GetMapping("/{chartId}/edit")
    fun getChartEdit(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.CHART_TYPE_P_CODE))
        model.addAttribute("operationList", codeService.selectCodeByParent(ChartConstants.CHART_OPERATION_P_CODE))
        model.addAttribute("unitList", codeService.selectCodeByParent(ChartConstants.CHART_UNIT_P_CODE))
        model.addAttribute("chart", chartService.getChart(chartId))
        return chartEditPage
    }

    /**
     * chart 보기 화면 호출
     */
    @GetMapping("/{chartId}/view")
    fun getChartView(@PathVariable chartId: String, model: Model): String {
        return chartViewPage
    }
}
