/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricPool.dto.MetricDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto

interface MetricPoolRepositoryCustom : AliceRepositoryCustom {

    fun findMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): PagingReturnDto

    fun findMetric(metricId: String): MetricDto

    fun findByMetricIds(metricIds: Set<String>): List<MetricLoadDto>
}
