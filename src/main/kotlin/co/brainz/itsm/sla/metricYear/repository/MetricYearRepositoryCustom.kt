/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.sla.metricYear.dto.MetricAnnualDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearDetailDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSimpleDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearViewData

interface MetricYearRepositoryCustom : AliceRepositoryCustom {

    fun existsByMetric(metricId: String): Boolean

    fun findMetrics(year: String): List<MetricYearViewData>

    fun existsByMetricAndMetricYear(metricId: String, metricYear: String): Boolean

    fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricYearSimpleDto>

    fun findMetricYearListByLoadCondition(year: String): List<MetricYearSimpleDto>

    fun findMetricYear(metricId: String, year: String): MetricYearDetailDto?

    fun getYears(): Set<String>

    fun findMetricStatusList(year: String): List<MetricAnnualDto>
}
