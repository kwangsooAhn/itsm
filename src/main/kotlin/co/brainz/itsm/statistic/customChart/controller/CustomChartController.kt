/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.controller

import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartSearchCondition
import co.brainz.itsm.statistic.customChart.service.CustomChartService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/statistics")
class CustomChartController(
    private val customChartService: CustomChartService,
    private val codeService: CodeService
) {

    private val customChartSearchPage: String = "statistic/customChart/customChartSearch"
    private val customChartListPage: String = "statistic/customChart/customChartList"
    private val customChartPage: String = "statistic/customChart/customChart"

    @Value("\${timezone.scheduler}")
    private val timezone: String? = null

    /**
     * 사용자 정의 차트 목록 검색 화면 호출
     */
    @GetMapping("/customChart/search")
    fun getCustomChartSearch(model: Model): String {
        model.addAttribute("typeList", codeService.selectCodeByParent(ChartConstants.PCode.TYPE.code))
        return customChartSearchPage
    }

    /**
     * 사용자 정의 차트 목록 화면 호출
     */
    @GetMapping("/customChart")
    fun getCustomCharts(chartSearchCondition: ChartSearchCondition, model: Model): String {
        val result = customChartService.getCharts(chartSearchCondition)
        model.addAttribute("chartList", result.data)
        model.addAttribute("paging", result.paging)
        return customChartListPage
    }

    /**
     * Chart 등록 화면 호출
     */
    @GetMapping("/customChart/new")
    fun getCustomChartNew(model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("code", customChartService.getCodeListForChart())
        model.addAttribute("unitList", codeService.selectCodeByParent(ChartConstants.PCode.UNIT.code))
        model.addAttribute("chartTimezone", timezone)
        return customChartPage
    }

    /**
     * Chart 수정 화면 호출
     */
    @GetMapping("/customChart/{chartId}/edit")
    fun getCustomChartEdit(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("view", false)
        model.addAttribute("code", customChartService.getCodeListForChart())
        model.addAttribute("chart", customChartService.getChartDetail(chartId))
        model.addAttribute("chartTimezone", timezone)
        return customChartPage
    }

    /**
     * chart 설정 보기 화면 호출
     */
    @GetMapping("/customChart/{chartId}/view")
    fun getCustomChartView(@PathVariable chartId: String, model: Model): String {
        model.addAttribute("view", true)
        model.addAttribute("code", customChartService.getCodeListForChart())
        model.addAttribute("chart", customChartService.getChartDetail(chartId))
        model.addAttribute("chartTimezone", timezone)
        return customChartPage
    }
}
