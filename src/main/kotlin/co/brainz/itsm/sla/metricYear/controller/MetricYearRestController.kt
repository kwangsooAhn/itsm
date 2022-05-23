/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.service.MetricYearService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metrics")
class MetricYearRestController(
    private val metricYearService: MetricYearService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 신규 연도별 지표 등록 처리
     */
    @PostMapping("/", "")
    fun insertMetric(@RequestBody metricYearDto: MetricYearDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricYearService.createMetricYear(metricYearDto))
    }

    /**
     * 년도 선택 시 해당년도에 저장된 지표목록 불러오기
     */
    @GetMapping("/", "")
    fun getMetricYearList(metricLoadCondition: MetricLoadCondition): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricYearService.getYearSaveMetricList(metricLoadCondition))
    }

    /**
     *  년도별 SLA 현황 엑셀 다운로드
     */
    @GetMapping("/yearlies/excel")
    fun getMetricExcel(metricYearSearchCondition: MetricYearSearchCondition): ResponseEntity<ByteArray> {
        return metricYearService.getMetricExcelDownload(metricYearSearchCondition)
    }
}
