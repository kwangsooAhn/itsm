/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto

interface MetricYearRepositoryCustom : AliceRepositoryCustom {

    fun existsByMetric(metricId: String): Boolean

    fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto>
}
