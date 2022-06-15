/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricPool.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricPool.dto.MetricData
import co.brainz.itsm.sla.metricPool.service.MetricPoolService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metric-pools")
class MetricPoolRestController(
    private val metricPoolService: MetricPoolService
) {
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 신규 지표 등록 처리
     */
    @PostMapping("/", "")
    fun insertMetric(@RequestBody metricData: MetricData): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricPoolService.createMetric(metricData))
    }

    /**
     * 지표 편집
     */
    @PutMapping("/{metricId}")
    fun updateMetric(@PathVariable metricId: String, @RequestBody metricData: MetricData): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricPoolService.updateMetric(metricId, metricData))
    }

    /**
     * 지표 삭제
     */
    @DeleteMapping("/{metricId}")
    fun deleteMetric(@PathVariable metricId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricPoolService.deleteMetric(metricId))
    }
}
