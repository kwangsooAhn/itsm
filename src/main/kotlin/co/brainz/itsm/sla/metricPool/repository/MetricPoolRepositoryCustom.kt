/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition

interface MetricPoolRepositoryCustom : AliceRepositoryCustom {

    fun findMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): PagingReturnDto
}
