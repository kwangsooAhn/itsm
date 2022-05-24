/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricStatus.service

import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import java.time.Year
import org.springframework.stereotype.Service

@Service
class MetricStatusService(
    private val metricYearRepository: MetricYearRepository
){

    fun getMetricList(): List<MetricLoadDto> {
        val metricLoadCondition = MetricLoadCondition(
            source = Year.now().toString()
        )
        return metricYearRepository.findMetricListByLoadCondition(metricLoadCondition)
    }
}
