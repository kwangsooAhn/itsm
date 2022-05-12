/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.sla.metricManual.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricManual.dto.MetricManualKeyDto
import co.brainz.itsm.sla.metricManual.service.MetricManualService
import java.time.LocalDate
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/sla/metric")
class MetricManualRestController(
    private val metricManualService: MetricManualService
) {

    /**
     * 년도 선택 시 해당년도에 저장된 지표목록 불러오기
     */
    @GetMapping("/", "")
    fun getMetricYearList(metricLoadCondition: MetricLoadCondition): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricManualService.getMetricYearList(metricLoadCondition))
    }

    /**
     * 수동지표 insert
     */
    @PostMapping("/manual", "")
    fun insertMetricManual(@RequestBody metricManualKeyDto: MetricManualKeyDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(metricManualService.insertMetricManual(metricManualKeyDto))
    }

    /**
     * 수동 지표 delete
     */
    @DeleteMapping("/manual/{metricId}/{referenceDt}/{metricValue}")
    fun deleteMetricManual(
        @PathVariable metricId: String,
        @PathVariable referenceDt: String,
        @PathVariable metricValue: Int
    ) : ResponseEntity<ZResponse>{
        return ZAliceResponse.response(metricManualService.deleteMetricManual(metricId,referenceDt,metricValue))
    }

}
