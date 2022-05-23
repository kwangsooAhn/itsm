/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearExcelDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition

interface MetricYearRepositoryCustom : AliceRepositoryCustom {

    fun existsByMetric(metricId: String): Boolean

    fun findMetrics(metricYearSearchCondition: MetricYearSearchCondition): PagingReturnDto

    fun existsByMetricAndMetricYear(metricId: String, metricYear: String): Boolean

    fun findMetricListByLoadCondition(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto>

    fun findMetricYearListForExcel(metricYearSearchCondition: MetricYearSearchCondition): List<MetricYearExcelDto>
}
