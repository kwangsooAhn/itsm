/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customChart.controller

import co.brainz.itsm.statistic.customChart.dto.ChartDto
import co.brainz.itsm.statistic.customChart.service.CustomChartService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/statistics")
class CustomChartRestController(private val customChartService: CustomChartService) {

    /**
     * 사용자 정의 차트 등록
     */
    @PostMapping("/customChart")
    fun createChart(@RequestBody chartDto: ChartDto): String {
        return customChartService.saveChart(chartDto)
    }

    /**
     * 사용자 정의 차트 수정
     */
    @PutMapping("/customChart/{chartId}")
    fun updateChart(@PathVariable chartId: String, @RequestBody chartDto: ChartDto): String {
        return customChartService.saveChart(chartDto)
    }

    /**
     * 사용자 정의 차트 삭제
     */
    @DeleteMapping("/customChart/{chartId}")
    fun deleteChart(@PathVariable chartId: String): String {
        return customChartService.deleteChart(chartId)
    }

    /**
     * 사용자 정의 차트 미리보기 데이터
     */
    @PostMapping("/customChart/{chartId}/preview")
    fun getPreviewChart(@PathVariable chartId: String, @RequestBody chartDto: ChartDto): ChartDto {
        return customChartService.getChartPreviewDetail(chartId, chartDto)
    }

    /**
     * 사용자 정의 차트 미리보기
     */
    @GetMapping("/customChart/{chartId}")
    fun getChart(@PathVariable chartId: String): ChartDto {
        return customChartService.getChartDetail(chartId)
    }
}
