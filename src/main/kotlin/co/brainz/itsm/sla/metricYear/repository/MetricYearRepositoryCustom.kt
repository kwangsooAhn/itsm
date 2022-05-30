/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearDetailDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearExcelDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition
import co.brainz.itsm.sla.metricYear.dto.MetricYearSimpleDto

interface MetricYearRepositoryCustom : AliceRepositoryCustom {

    fun existsByMetric(metricId: String): Boolean

    fun findMetrics(metricYearSearchCondition: MetricYearSearchCondition): PagingReturnDto

    fun existsByMetricAndMetricYear(metricId: String, metricYear: String): Boolean

    fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricYearSimpleDto>

    fun findMetricYearListForExcel(metricYearSearchCondition: MetricYearSearchCondition): List<MetricYearExcelDto>

    fun findMetricYear(metricId: String, year: String): MetricYearDetailDto

    fun getYears(): Set<String>

    fun findByMetricIds(): MutableSet<String>
}
