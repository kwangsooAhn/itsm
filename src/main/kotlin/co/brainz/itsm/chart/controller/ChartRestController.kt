/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.controller

import co.brainz.itsm.chart.dto.ChartDto
import co.brainz.itsm.chart.service.ChartService
import org.springframework.web.bind.annotation.DeleteMapping
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
     * 통계 차트 등록
     */
    @PostMapping("")
    fun createChart(@RequestBody chartDto: ChartDto): String {
        return chartService.saveChart(chartDto)
    }

    /**
     * 통계 차트 수정
     */
    @PutMapping("/{chartId}")
    fun updateChart(@PathVariable chartId: String, @RequestBody chartDto: ChartDto): String {
        return chartService.saveChart(chartDto)
    }

    /**
     * 통계 차트 삭제
     */
    @DeleteMapping("/{chartId}")
    fun deleteChart(@PathVariable chartId: String): String {
        return chartService.deleteChart(chartId)
    }

    /**
     * 통계 차트 미리보기 데이터
     */
    @PostMapping("/{chartId}/preview")
    fun chartPreview(@PathVariable chartId: String, @RequestBody chartDto: ChartDto): ChartDto {
        return chartService.getPreviewChart(chartId, chartDto)
    }
}
