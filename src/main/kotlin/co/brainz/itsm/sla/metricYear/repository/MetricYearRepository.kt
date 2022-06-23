package co.brainz.itsm.sla.metricYear.repository

import co.brainz.itsm.sla.metricPool.entity.MetricPoolEntity
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntity
import co.brainz.itsm.sla.metricYear.entity.MetricYearEntityPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricYearRepository : JpaRepository<MetricYearEntity, MetricYearEntityPk>, MetricYearRepositoryCustom {

    fun findByMetricAndMetricYear(metric: MetricPoolEntity, metricYear: String): MetricYearEntity

    fun findByMetricYear(metricYear: String): List<MetricYearEntity>
}
