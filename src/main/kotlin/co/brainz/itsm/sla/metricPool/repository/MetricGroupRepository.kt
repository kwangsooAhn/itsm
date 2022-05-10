package co.brainz.itsm.sla.metricPool.repository

import co.brainz.itsm.sla.metricPool.entity.MetricGroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MetricGroupRepository : JpaRepository<MetricGroupEntity, String> {

    override fun findAll(): MutableList<MetricGroupEntity>
}
