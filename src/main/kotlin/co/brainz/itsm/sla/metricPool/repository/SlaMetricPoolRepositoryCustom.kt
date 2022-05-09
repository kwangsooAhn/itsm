/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.sla.metricPool.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.portal.dto.PortalTopDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolListDto
import co.brainz.itsm.sla.metricPool.dto.MetricPoolSearchCondition
import com.querydsl.core.QueryResults

interface SlaMetricPoolRepositoryCustom : AliceRepositoryCustom {

    fun findMetricPools(metricPoolSearchCondition: MetricPoolSearchCondition): PagingReturnDto
}
