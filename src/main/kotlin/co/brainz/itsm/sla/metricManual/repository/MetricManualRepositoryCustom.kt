/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition
import co.brainz.itsm.sla.metricManual.dto.MetricManualSimpleDto

interface MetricManualRepositoryCustom : AliceRepositoryCustom {
    fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): PagingReturnDto
    fun findMetricByMetricType(metricType: String): List<MetricManualSimpleDto>

    fun existsByMetric(metricId: String): Boolean
}
