/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.controller

import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.service.ChartService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/charts")
class ChartRestController(private val chartService: ChartService) {

    /**
     * 사용자 정의 차트 등록
     */
    @PostMapping("")
    fun createChart(@RequestBody chartDto: ChartDto): String {
        return chartService.saveChart(chartDto)
    }

    /**
     * 사용자 정의 차트 수정
     */
    @PutMapping("/{chartId}")
    fun updateChart(@PathVariable chartId: String, @RequestBody chartDto: ChartDto): String {
        return chartService.saveChart(chartDto)
    }

    /**
     * 사용자 정의 차트 삭제
     */
    @DeleteMapping("/{chartId}")
    fun deleteChart(@PathVariable chartId: String): String {
        return chartService.deleteChart(chartId)
    }

    /**
     * 사용자 정의 차트 미리보기 데이터
     */
    @PostMapping("/{chartId}/preview")
    fun getPreviewChart(@PathVariable chartId: String, @RequestBody chartDto: ChartDto): ChartDto {
        return chartService.getChartPreviewDetail(chartId, chartDto)
    }

    /**
     * 사용자 정의 차트 미리보기
     */
    @GetMapping("/{chartId}")
    fun getChart(@PathVariable chartId: String): ChartDto {
        return chartService.getChartDetail(chartId)
    }
}
