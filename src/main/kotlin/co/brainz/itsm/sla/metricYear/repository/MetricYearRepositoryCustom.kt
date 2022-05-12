package co.brainz.itsm.sla.metricYear.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.sla.metricYear.dto.MetricLoadCondition
import co.brainz.itsm.sla.metricYear.dto.MetricLoadDto

interface MetricYearRepositoryCustom : AliceRepositoryCustom {
    fun findMetricYearList(metricLoadCondition: MetricLoadCondition): List<MetricLoadDto>
}
