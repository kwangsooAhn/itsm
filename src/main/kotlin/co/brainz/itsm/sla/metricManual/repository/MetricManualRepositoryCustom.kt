/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.sla.metricManual.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition

interface MetricManualRepositoryCustom : AliceRepositoryCustom {
    fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): PagingReturnDto
}
