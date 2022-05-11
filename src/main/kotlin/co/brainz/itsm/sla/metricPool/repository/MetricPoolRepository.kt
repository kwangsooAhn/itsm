package co.brainz.itsm.sla.metricPool.repository

import co.brainz.itsm.sla.metricPool.entity.MetricEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricPoolRepository : JpaRepository<MetricEntity, String>, MetricPoolRepositoryCustom {

    fun existsByMetricName(metricName: String): Boolean

    fun findByMetricId(metricName: String): MetricEntity
}
