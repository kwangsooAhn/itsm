package co.brainz.itsm.sla.metricManual.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.sla.metricManual.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricManual.dto.MetricLoadDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualKeyDto
import co.brainz.itsm.sla.metricManual.dto.MetricManualSearchCondition

interface MetricManualRepositoryCustom : AliceRepositoryCustom {
    fun findMetricManualSearch(manualSearchCondition: MetricManualSearchCondition): PagingReturnDto

    fun findMetricYearList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto>

    fun existsByMetricIdAndReferenceDt(metricManualKeyDto: MetricManualKeyDto): Boolean
}
