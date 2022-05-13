/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricYear.dto.MetricYearSearchCondition

interface MetricYearRepositoryCustom : AliceRepositoryCustom {

    fun existsByMetric(metricId: String): Boolean

    fun findMetrics(metricYearSearchCondition: MetricYearSearchCondition): PagingReturnDto
}
