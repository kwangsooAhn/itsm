/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.controller

import co.brainz.itsm.sla.metricStatus.service.MetricStatusService
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metrics")
class MetricStatusRestController(
    private val metricStatusService: MetricStatusService
) {

    /**
     *  년도별 SLA 현황 엑셀 다운로드
     */
    @GetMapping("/yearlies/excel")
    fun getMetricYearlyExcel(metricYearSearchCondition: MetricYearSearchCondition): ResponseEntity<ByteArray> {
        return metricStatusService.getMetricYearlyExcelDownload(metricYearSearchCondition)
    }
}
