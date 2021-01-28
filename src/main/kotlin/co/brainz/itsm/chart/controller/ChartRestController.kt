/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.controller

import co.brainz.itsm.chart.dto.ChartListDto
import co.brainz.itsm.chart.service.ChartService
import javax.servlet.http.HttpServletRequest
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/charts")
class ChartRestController(private val chartService: ChartService) {

    /**
     * Chart 리스트 조회
     */
    @GetMapping("")
    fun getCharts(request: HttpServletRequest, model: Model): List<ChartListDto> {
        val searchTypeName = request.getParameter("searchTypeName")
        val offset = request.getParameter("offset") ?: "0"
        return chartService.getCharts(searchTypeName, offset)
    }

    /**
     * Chart 등록
     */
    @PostMapping("")
    fun createChart() {

    }

    /**
     * Chart 수정
     */
    @PutMapping("/{chartId}")
    fun updateChart() {

    }

    /**
     * Chart 삭제
     */
    @DeleteMapping("/{chartId")
    fun deleteChart() {

    }
}
