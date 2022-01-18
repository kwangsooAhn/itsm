/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.chart.controller

import javax.servlet.http.HttpServletRequest
import co.brainz.itsm.report.chart.constants.ChartConstants
import co.brainz.itsm.report.chart.dto.ChartSearchCondition
import co.brainz.itsm.report.chart.service.ChartService
import co.brainz.itsm.code.service.CodeService
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
    private val chartPage: String = "chart/chart"
    private val basicChartSearchPage: String = "chart/basicChartSearch"

    /**
     * 사용자 정의 차트 목록 검색 화면 호출
     */
    @GetMapping("/customChart/search")
    fun getChartSearch(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.PCode.TYPE.code))
        return chartSearchPage
    }

    /**
     * 사용자 정의 차트 목록 화면 호출
     */
    @GetMapping("/customChart")
    fun getCharts(chartSearchCondition: ChartSearchCondition, model: Model): String {
        val result = chartService.getCharts(chartSearchCondition)
        model.addAttribute("chartList", result.data)
        model.addAttribute("paging", result.paging)
        return chartListPage
    }

    /**
     * Chart 등록 화면 호출
     */
    @GetMapping("/customChart/new")
    fun getChartNew(model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("code", chartService.getCodeListForChart())
        model.addAttribute("unitList", codeService.selectCodeByParent(ChartConstants.PCode.UNIT.code))
        return chartPage
    }

    /**
     * Chart 수정 화면 호출
     */
    @GetMapping("/customChart/{chartId}/edit")
    fun getChartEdit(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("code", chartService.getCodeListForChart())
        model.addAttribute("chart", chartService.getChartDetail(chartId))
        return chartPage
    }

    /**
     * chart 설정 보기 화면 호출
     */
    @GetMapping("/customChart/{chartId}/view")
    fun getChartView(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("view", true)
        model.addAttribute("code", chartService.getCodeListForChart())
        model.addAttribute("chart", chartService.getChartDetail(chartId))
        return chartPage
    }

    @GetMapping("/basicChart/search")
    fun getBasicChartSearch(request: HttpServletRequest, model: Model): String {
        return basicChartSearchPage
    }
}
