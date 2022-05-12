/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricYear.service

import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.repository.MetricYearRepository
import org.springframework.stereotype.Service

@Service
class MetricYearService(
    private val metricYearRepository: MetricYearRepository
) {
    fun getMetricYearList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto> {
        return metricYearRepository.findMetricYearList(metricLoadCondition)
    }

}
