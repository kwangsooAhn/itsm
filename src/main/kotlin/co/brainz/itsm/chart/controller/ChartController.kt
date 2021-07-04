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
import org.springframework.web.bind.annotation.RequestParam

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
     * 사용자 정의 차트 목록 검색 화면 호출
     */
    @GetMapping("/search")
    fun getChartSearch(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.PCode.TYPE.code))
        return chartSearchPage
    }

    /**
     * 사용자 정의 차트 목록 화면 호출
     */
    @GetMapping("")
    fun getCharts(
        request: HttpServletRequest,
        @RequestParam(value = "isScroll", required = false) isScroll: Boolean,
        model: Model
    ): String {
        val searchTypeName = request.getParameter("searchTypeName")
        val offset = request.getParameter("offset") ?: "0"
        val result = chartService.getCharts(searchTypeName, offset)
        model.addAttribute("chartList", result.data)
        model.addAttribute("chartListCount", result.totalCount)
        return if (isScroll) chartListFragment else chartListPage
    }

    /**
     * Chart 등록 화면 호출
     */
    @GetMapping("/new")
    fun getChartNew(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.PCode.TYPE.code))
        model.addAttribute("operationList", codeService.selectCodeByParent(ChartConstants.PCode.OPERATION.code))
        model.addAttribute("unitList", codeService.selectCodeByParent(ChartConstants.PCode.UNIT.code))
        return chartEditPage
    }

    /**
     * Chart 수정 화면 호출
     */
    @GetMapping("/{chartId}/edit")
    fun getChartEdit(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.PCode.TYPE.code))
        model.addAttribute("operationList", codeService.selectCodeByParent(ChartConstants.PCode.OPERATION.code))
        model.addAttribute("unitList", codeService.selectCodeByParent(ChartConstants.PCode.UNIT.code))
        model.addAttribute("chart", chartService.getChartDetail(chartId, null))
        return chartEditPage
    }

    /**
     * chart 보기 화면 호출
     */
    @GetMapping("/{chartId}/view")
    fun getChartView(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("chart", chartService.getChartDetail(chartId, null))
        return chartViewPage
    }
}
