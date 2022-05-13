/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricManual.dto.MetricManualDataDto
import co.brainz.itsm.sla.metricManual.service.MetricManualService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metrics/manual")
class MetricManualRestController(
    private val metricManualService: MetricManualService
) {
    /**
     * 수동지표 insert
     */
    @PostMapping("/", "")
    fun insertMetricManual(@RequestBody metricManualDataDto: MetricManualDataDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricManualService.insertMetricManual(metricManualDataDto))
    }

    /**
     * 수동 지표 delete
     */
    @DeleteMapping("/{metricManualId}")
    fun deleteMetricManual(
        @PathVariable metricManualId: String
    ): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricManualService.deleteMetricManual(metricManualId))
    }
}
